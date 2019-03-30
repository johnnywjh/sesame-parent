package kim.sesame.framework.define.notice;

/**
 * 通知业务接口
 */
public interface INoticeService {

    /**
     * 同步发送通知,并返回结果
     *
     * @param notice
     * @return
     */
    boolean sendNotice(NoticeEntity notice);

    /**
     * 异步发送通知
     *
     * @param notice
     */
    default void sendNoticeAsyn(NoticeEntity notice) {
        new Thread(() -> {
            this.sendNotice(notice);
        });
    }

}
