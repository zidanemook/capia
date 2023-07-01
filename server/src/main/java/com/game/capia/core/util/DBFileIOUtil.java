package com.game.capia.core.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.RunScript;

public class DBFileIOUtil {

    // 주기적으로 수행될 백업 코드
    public static void backupDatabase() throws SQLException {
        // 데이터베이스로부터 연결 가져오기
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "capia", "");

        String script = "backup to 'backup.zip'";
        Reader reader = new StringReader(script);
        // BACKUP 명령 실행
        RunScript.execute(conn, reader);
    }

    // 서버 재시작 시 복구 코드
    public static void restoreDatabase() throws SQLException, FileNotFoundException {
        // 데이터베이스로부터 연결 가져오기
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "capia", "");

        // BACKUP에서 복구
        RunScript.execute(conn, new FileReader("backup.zip"));
    }
}
