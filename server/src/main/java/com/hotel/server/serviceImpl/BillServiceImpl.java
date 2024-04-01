package com.hotel.server.serviceImpl;

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
import java.util.Date;
import java.util.List;
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

    @DubboReference
    private RoomService roomService;

    @DubboReference(check = false)
    private CustomerService customerService;

    @DubboReference(check = false)
    private TimerService timerService;

    @Override
    public BillStatementResp getBillStatement(String customerId) {
        Customer customer = customerService.getById(customerId);
        if (customer == null) {
            return getLeavedBillStatement(customerId);
        }

        Room room = roomService.getById(customer.getRoom());
        BillStatementResp resp = BillStatementResp.builder().customerId(customerId)
                .roomId(String.valueOf(room.getId())).checkInTime(customer.getStartTime())
                .roomPrice(room.getPrice()).deposit(room.getDeposit()).build();

        List<CustomerFood> customerFoods = customerFoodDao.selectAll(customerId);
        List<CustomerAC> customerACs = customerACDao.selectAll(customerId);
        resp.setFoodBillList(customerFoods);
        resp.setAcBillList(customerACs);

        BigDecimal roomPrice = calRoomPrice(customer.getStartTime(), timerService.getTime(), room.getPrice());
        BigDecimal foodPrice = calFoodPrice(customerFoods);
        BigDecimal acPrice = calACPrice(customerACs);

        BigDecimal totPrice = roomPrice.add(foodPrice.add(acPrice)).min(new BigDecimal(room.getDeposit()));

        resp.setTotalPrice(totPrice.toString());
        return resp;
    }

    /**
     * 查询已经退房用户的详单
     */
    private BillStatementResp getLeavedBillStatement(String customerId) {
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
    public BillResp getBill(String customerId) {
        BillStatementResp billStatement = this.getBillStatement(customerId);
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
        // todo
        return false;
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
}
