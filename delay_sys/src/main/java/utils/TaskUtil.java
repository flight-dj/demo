package utils;

import org.apache.rocketmq.client.producer.SendResult;
import timing.template.BusinessTask;

public class TaskUtil{
    /***
     * 执行任务,校验是否执行成功
     * @param businessTask 任务内容[1:成功][0:失败]
     * @return
     */
    private static boolean execTask(final BusinessTask businessTask){
        //如果为空认为是无效任务自动认为执行成功
        if(businessTask == null){
            return true;
        }
        //如果url为空则任务是无效任务,自动认为执行成功
        if(BaseUtil.isBlank(businessTask.getUrl())){
            return true;
        }

        SendResult sendResult = SendMessageUtil.sendMessage(businessTask.getMessage());

        if(sendResult == null){
            return false;
        }

        return true;
    }

    /***
     * 执行任务
     * @param businessTask 任务
     */
    public static boolean execTaskAnsy(final BusinessTask businessTask){
        return execTask(businessTask);
    }

}
