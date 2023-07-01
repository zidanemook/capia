package com.game.capia.core.listener;

import com.game.capia.core.util.DBFileIOUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Profile("prod")
public class ShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 애플리케이션 종료 시 실행할 작업을 여기에 작성합니다
        try {
            DBFileIOUtil.backupDatabase();
        } catch (SQLException e) {
            // 예외 처리
        }
    }
}
