package com.game.capia.core.runner;

import com.game.capia.core.util.DBFileIOUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Profile("prod")
public class StartupHandler implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 애플리케이션 시작 시 실행할 작업을 여기에 작성합니다
        try {
            DBFileIOUtil.backupDatabase();
        } catch (SQLException e) {
            // 예외 처리
        }
    }
}
