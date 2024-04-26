package com.hotel.server;

import com.alibaba.fastjson2.JSON;
import com.hotel.common.constants.BillType;
import com.hotel.common.dto.response.BillResp;
import com.hotel.common.dto.response.BillStatementResp;
import com.hotel.common.entity.CustomerAC;
import com.hotel.common.entity.CustomerFood;
import com.hotel.common.entity.Food;
import com.hotel.common.service.server.BillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 详单测试
 *
 * @author: 郭晨旭
 * @create: 2024-04-16 16:15
 * @version: 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class BillStatementTest {

    @DubboReference
    private BillService billService;

    @Test
    public void testOutputPDF() throws IOException {
        BillStatementResp resp = new BillStatementResp();
        resp.setCustomerId("123");
        resp.setRoomId("222");
        resp.setCheckInTime(new Date());
        resp.setRoomPrice("2121");
        resp.setRoomTotPrice("14739280");
        resp.setDeposit("74382");
        resp.setFoodPrice("791802435");
        resp.setAcPrice("301-872");
        resp.setTotalPrice("71908324");

        Food food = Food.builder().name("food").price("31").build();
        Map<String, Integer> map = new HashMap<>();
        map.put(JSON.toJSONString(food), 1);
        food.setPrice("111");
        map.put(JSON.toJSONString(food), 2);
        CustomerFood cf = CustomerFood.builder().foods(map).totalPrice("1378924").remarks("123").createAt(new Date()).build();
        resp.setFoodBillList(new ArrayList<>());
        resp.getFoodBillList().add(cf);
        resp.getFoodBillList().add(cf);

        CustomerAC customerAC = CustomerAC.builder().price("1234").status(1).changeTemperature(1.0).duration(12).totalPrice("123").createAt(new Date()).build();
        resp.setAcBillList(new ArrayList<>());
        resp.getAcBillList().add(customerAC);
        resp.getAcBillList().add(customerAC);

//        billService.outputPDF(resp, new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));
        byte[] bytes = billService.generateBillStatementPDF(resp, new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));

        try (FileOutputStream outputStream = new FileOutputStream("d:/pdf.pdf")) {
            outputStream.write(bytes);
        }
    }

    @Test
    public void testGenerateBillPDF() throws IOException {
        BillResp resp = BillResp.builder().customerId("123").build();
        byte[] bytes = billService.generateBillPDF(resp);
        try (FileOutputStream outputStream = new FileOutputStream("d:/pdf.pdf")) {
            outputStream.write(bytes);
        }
    }

    @Test
    public void testBigDecimal() {
        BigDecimal u = new BigDecimal("100");
        BigDecimal divide = new BigDecimal("200").divide(u, 0, RoundingMode.FLOOR);
        log.info("200: {}", Integer.parseInt(String.valueOf(divide)));
        BigDecimal divide1 = new BigDecimal("201").divide(u, 0, RoundingMode.FLOOR);
        log.info("201: {}", divide1);
        BigDecimal divide2 = new BigDecimal("299").divide(u, 0, RoundingMode.FLOOR);
        log.info("299: {}", divide2);
        BigDecimal divide3 = new BigDecimal("300").divide(u, 0, RoundingMode.FLOOR);
        log.info("300: {}", divide3);
        List<String> prices = Arrays.asList("100", "200", "201", "299", "299.99", "999", "150", "80");

        Map<Integer, Integer> map = new HashMap<>();
        prices.forEach((e) -> {
            BigDecimal d = new BigDecimal(e).divide(u, 0, RoundingMode.FLOOR);
            map.put(d.intValue() * 100, map.getOrDefault(d.intValue() * 100, 0) + 1);
        });
        log.info("{}", map);
    }

    @Test
    public void testGenBill() {
        billService.getBill("1781186036103704578", new HashSet<>(Arrays.asList(BillType.AC, BillType.ROOM, BillType.FOOD)));
    }
}
