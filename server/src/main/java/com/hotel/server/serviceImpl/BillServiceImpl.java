package com.hotel.server.serviceImpl;

import com.hotel.common.utils.PDFUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.text.SimpleDateFormat;

import com.hotel.common.constants.BillType;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.entity.*;
import com.hotel.common.service.customer.CustomerService;
import com.hotel.common.service.server.BillService;
import com.hotel.common.service.server.RoomService;
import com.hotel.common.service.timer.TimerService;
import com.hotel.server.dao.BillStatementDao;
import com.hotel.server.dao.CustomerACDao;
import com.hotel.server.dao.CustomerFoodDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 财务服务实现类
 *
 * @author: 郭晨旭
 * @create: 2024-04-01 17:26
 * @version: 1.0
 */
@Slf4j
@DubboService
public class BillServiceImpl implements BillService {

    @Resource
    private BillStatementDao billStatementDao;

    @Resource
    private CustomerACDao customerACDao;

    @Resource
    private CustomerFoodDao customerFoodDao;

    @DubboReference(check = false)
    private RoomService roomService;

    @DubboReference(check = false)
    private CustomerService customerService;

    @DubboReference(check = false)
    private TimerService timerService;

    @Override
    public String ping(String id) {
        return "pong " + id;
    }

    @Override
    public BillStatementResp getBillStatement(String customerId, Set<String> types) {
        Customer customer = customerService.getById(customerId);
        if (customer == null) {
            return getLeavedBillStatement(customerId, types);
        }

        Room room = roomService.getById(customer.getRoom());
        BillStatementResp resp = BillStatementResp.builder().customerId(customerId)
                .roomId(String.valueOf(room.getId())).build();

        // 房间费用
        if (types.contains(BillType.ROOM)) {
            resp.setRoomPrice(room.getPrice());
            resp.setDeposit(room.getDeposit());
            resp.setCheckInTime(customer.getStartTime());

            BigDecimal roomPrice = calRoomPrice(customer.getStartTime(), timerService.getTime(), room.getPrice());
            resp.setRoomTotPrice(String.valueOf(roomPrice));
        }

        // 空调费用
        if (types.contains(BillType.AC)) {
            List<CustomerAC> customerACs = customerACDao.selectAll(customerId);
            resp.setAcBillList(customerACs);

            BigDecimal acPrice = calACPrice(customerACs);
            resp.setAcPrice(String.valueOf(acPrice));
        }

        // 餐饮费用
        if (types.contains(BillType.FOOD)) {
            List<CustomerFood> customerFoods = customerFoodDao.selectAll(customerId);
            resp.setFoodBillList(customerFoods);

            BigDecimal foodPrice = calFoodPrice(customerFoods);
            resp.setFoodPrice(String.valueOf(foodPrice));
        }

        if (types.contains(BillType.ROOM) && types.contains(BillType.AC) && types.contains(BillType.FOOD)) {
            BigDecimal roomTot = new BigDecimal(resp.getRoomTotPrice());
            BigDecimal acTot = new BigDecimal(resp.getAcPrice());
            BigDecimal foodTot = new BigDecimal(resp.getFoodPrice());
            BigDecimal totPrice = roomTot.add(foodTot.add(acTot)).min(new BigDecimal(room.getDeposit()));
            resp.setTotalPrice(totPrice.toString());
        }

        return resp;
    }

    /**
     * 查询已经退房用户的详单
     */
    private BillStatementResp getLeavedBillStatement(String customerId, Set<String> types) {
        List<BillStatement> billStatements = billStatementDao.selectAll(customerId);
        if (CollectionUtils.isEmpty(billStatements)) {
            throw new IllegalArgumentException("顾客不存在");
        }

        BillStatement billStatement = billStatements.get(0);
        BillStatementResp resp = new BillStatementResp();
        BeanUtils.copyProperties(billStatement, resp);
        return resp;
    }

    @Override
    public BillResp getBill(String customerId, Set<String> types) {
        BillStatementResp billStatement = this.getBillStatement(customerId, types);
        BillResp resp = BillResp.builder().customerId(customerId).roomId(billStatement.getRoomId())
                .checkInTime(billStatement.getCheckInTime()).checkOutTime(billStatement.getCheckOutTime())
                .deposit(billStatement.getDeposit()).totalPrice(billStatement.getTotalPrice()).build();
        resp.setRoomTotPrice(calRoomPrice(billStatement.getCheckInTime(),
                billStatement.getCheckOutTime() == null
                        ? timerService.getTime()
                        : billStatement.getCheckOutTime()
                , billStatement.getRoomPrice()).toString());
        resp.setFoodBill(calFoodPrice(billStatement.getFoodBillList()).toString());
        resp.setAcBill(calACPrice(billStatement.getAcBillList()).toString());
        return resp;
    }

    @Override
    public Boolean saveBillStatement(String customerId) {
        BillStatementResp billStatementResp = this.getBillStatement(customerId, new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));
        BillStatement billStatement = new BillStatement();
        BeanUtils.copyProperties(billStatementResp, billStatement);
        billStatementDao.save(billStatement);
        return true;
    }

    @Override
    public void saveACBill(CustomerAC customerAC) {
        customerACDao.save(customerAC);
    }

    /**
     * 计算房费
     */
    private BigDecimal calRoomPrice(Date startTime, Date endTime, String price) {
        long diffInMilliseconds = endTime.getTime() - startTime.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        long nights = diffInDays - 1;
        return new BigDecimal(price).multiply(new BigDecimal(nights));
    }

    /**
     * 计算餐饮总费用
     */
    private BigDecimal calFoodPrice(List<CustomerFood> customerFoods) {
        BigDecimal res = BigDecimal.ZERO;
        for (CustomerFood customerFood : customerFoods) {
            res = res.add(new BigDecimal(customerFood.getTotalPrice()));
        }
        return res;
    }

    /**
     * 计算空调总费用
     */
    private BigDecimal calACPrice(List<CustomerAC> customerACs) {
        BigDecimal res = BigDecimal.ZERO;
        for (CustomerAC customerAC : customerACs) {
            res = res.add(new BigDecimal(customerAC.getTotalPrice()));
        }
        return res;
    }


    @Override
    public void outputBillStatementPDF(BillStatementResp billStatement, Set<String> types, OutputStream output) throws IOException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, output);
            document.open();

            Customer customer = customerService.getById(billStatement.getCustomerId());

            PdfPTable table = new PdfPTable(2);

            PDFUtil.addRow(table, "房间ID", billStatement.getRoomId());
            PDFUtil.addRow(table, "顾客ID", billStatement.getCustomerId());
            PDFUtil.addRow(table, "顾客姓名", customer.getName());

            if (types.contains(BillType.ROOM)) {
                PDFUtil.addRow(table, "  ", "");
                PDFUtil.addRow(table, "房间费用: ", "");
                PDFUtil.addRow(table, "入住时间", formatDate(billStatement.getCheckInTime()));
                if (billStatement.getCheckOutTime() != null) {
                    PDFUtil.addRow(table, "退房时间", formatDate(billStatement.getCheckOutTime()));
                }
                PDFUtil.addRow(table, "房间每晚价格", billStatement.getRoomPrice());
                PDFUtil.addRow(table, "房间押金", billStatement.getDeposit());
                PDFUtil.addRow(table, "截至目前的房费总价", billStatement.getRoomTotPrice());
            }

            if (types.contains(BillType.FOOD)) {
                PDFUtil.addRow(table, "  ", "");
                PDFUtil.addRow(table, "餐饮费用: ", "");
                int cnt = 1;
                for (CustomerFood food : billStatement.getFoodBillList()) {
                    PDFUtil.addRow(table, String.valueOf(cnt), "");
                    PDFUtil.addRow(table, "  点餐列表", "");
                    food.getFoods().forEach((k, v) -> {
                        PDFUtil.addRow(table, "    名字", k.getName());
                        PDFUtil.addRow(table, "    价格", k.getPrice());
                        PDFUtil.addRow(table, "    数量", v.toString());
                    });
                    PDFUtil.addRow(table, "  备注", food.getRemarks());
                    PDFUtil.addRow(table, "  此次点餐总价", food.getTotalPrice());
                    PDFUtil.addRow(table, "  点餐时间", formatDate(food.getCreateAt()));
                    cnt++;
                }
                PDFUtil.addRow(table, "餐饮总价", billStatement.getFoodPrice());
            }

            if (types.contains(BillType.AC)) {
                PDFUtil.addRow(table, "  ", "");
                PDFUtil.addRow(table, "空调费用: ", "");
                int cnt = 1;
                for (CustomerAC ac : billStatement.getAcBillList()) {
                    PDFUtil.addRow(table, String.valueOf(cnt), "");
                    PDFUtil.addRow(table, "  空调价格", ac.getPrice());
                    PDFUtil.addRow(table, "  空调状态", ac.getStatus().toString());
                    PDFUtil.addRow(table, "  空调风速 (度/分钟)", ac.getChangeTemperature().toString());
                    PDFUtil.addRow(table, "  空调使用时长", ac.getDuration().toString());
                    PDFUtil.addRow(table, "  此次服务总价", ac.getTotalPrice());
                    PDFUtil.addRow(table, "  使用开始时间", formatDate(ac.getCreateAt()));
                    cnt++;
                }
                PDFUtil.addRow(table, "空调总价", billStatement.getAcPrice());
            }

            if (types.contains(BillType.ROOM) && types.contains(BillType.AC) && types.contains(BillType.FOOD)) {
                PDFUtil.addRow(table, "  ", "");
                PDFUtil.addRow(table, "扣除押金后总价", billStatement.getTotalPrice());
            }

            document.add(table);
        } catch (Exception e) {
            log.error("生成PDF文件失败", e);
            throw new RuntimeException("生成PDF文件失败");
        } finally {
            log.info("生成pdf结束, 关闭文件流");
            document.close();
        }
    }

    @Override
    public void outputBillPDF(BillResp bill, OutputStream output) throws IOException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, output);
            document.open();

            Customer customer = customerService.getById(bill.getCustomerId());

            PdfPTable table = new PdfPTable(2);

            PDFUtil.addRow(table, "房间ID", bill.getRoomId());
            PDFUtil.addRow(table, "顾客ID", bill.getCustomerId());
            PDFUtil.addRow(table, "顾客姓名", customer.getName());

            PDFUtil.addRow(table, "入住时间", formatDate(bill.getCheckInTime()));
            if (bill.getCheckOutTime() != null) {
                PDFUtil.addRow(table, "退房时间", formatDate(bill.getCheckOutTime()));
            }
            PDFUtil.addRow(table, "房间押金", bill.getDeposit());
            PDFUtil.addRow(table, "截至目前的房费总价", bill.getRoomTotPrice());

            PDFUtil.addRow(table, "餐饮费用", bill.getFoodBill());
            PDFUtil.addRow(table, "空调费用", bill.getAcBill());

            PDFUtil.addRow(table, "扣除押金后总价", bill.getTotalPrice());

            document.add(table);
        } catch (
                Exception e) {
            log.error("生成PDF文件失败", e);
            throw new RuntimeException("生成PDF文件失败");
        } finally {
            log.info("生成pdf结束, 关闭文件流");
            document.close();
        }

    }


    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
