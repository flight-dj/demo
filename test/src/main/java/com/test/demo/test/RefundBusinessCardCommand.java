package com.test.demo.test;

import lombok.Data;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;

/**
 * @Description:
 * @Author: dajun
 * @Date: 2020/9/14 8:20 下午
 **/
public class RefundBusinessCardCommand implements Command {
    public boolean execute(Context context) throws Exception {
        RefundContext refundContext = (RefundContext) context;
        System.out.println("orderId: " + refundContext.getOrderId() + " 退款开始,第一步：退商旅卡，金额：10");
        return false;
    }
}
class RefundCashCommand implements Command {

    public boolean execute(Context context) throws Exception {
        RefundContext refundContext = (RefundContext) context;
        System.out.println("orderId: " + refundContext.getOrderId() +" 退款开始,第二步：退现金，金额：5");
        return false;
    }
}

class RefundPromotionCommand implements Command{

    public boolean execute(Context context) throws Exception {
        RefundContext refundContext = (RefundContext) context;
        System.out.println("orderId: " + refundContext.getOrderId() + " 退款开始,第二步：退优惠券，金额：20");
        return false;
    }
}


@Data
class RefundContext extends ContextBase {

    /**
     * 订单号
     */
    private Integer orderId;

}

class RefundTicketChain extends ChainBase {

    public void init() {
        //退商旅卡
        this.addCommand(new RefundBusinessCardCommand());
        //退现金
        this.addCommand(new RefundCashCommand());
        //退优惠券
        this.addCommand(new RefundPromotionCommand());
    }

    public static void main(String[] args) throws Exception {
        RefundTicketChain refundTicketChain = new RefundTicketChain();
        refundTicketChain.init();
        RefundContext context = new RefundContext();
        context.setOrderId(1621940242);
        refundTicketChain.execute(context);
    }

}