package com.cnblogs.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class OrderTools {

    @Tool("查询订单状态")
    public String getOrderStatus(@P("订单ID") String orderId) {
        // 模拟查询订单状态的逻辑
        return "Order: " + orderId + " is in processing";
    }   
}
