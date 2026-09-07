package com.springboot.sqlinjectionlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公开实验区：Union/布尔/时间/报错接口只执行 CALL，参数拼接位于 MySQL 存储过程内部，仅用于本地安全练习。
 */
@Controller
public class LabController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/lab")
    public String labHome() {
        return "lab/index";
    }

    @GetMapping("/lab/union")
    public String unionSearch(@RequestParam(value = "q", defaultValue = "") String q, Model model) {
        model.addAttribute("q", q);
        runIntoModel("CALL lab_search_users(?)", model, q);
        return "lab/union";
    }

    @GetMapping("/lab/sql/error")
    public String errorBased(@RequestParam(value = "id", defaultValue = "1") String id, Model model) {
        model.addAttribute("idInput", id);
        runIntoModel("CALL lab_user_by_id(?)", model, id);
        return "lab/sql_error";
    }

    @GetMapping("/lab/sql/boolean")
    public String booleanBlind(@RequestParam(value = "id", defaultValue = "1") String id, Model model) {
        model.addAttribute("idInput", id);
        runIntoModel("CALL lab_user_by_id(?)", model, id);
        return "lab/sql_boolean";
    }

    @GetMapping("/lab/sql/time")
    public String timeBlind(@RequestParam(value = "id", defaultValue = "1") String id, Model model) {
        model.addAttribute("idInput", id);
        long started = System.nanoTime();
        runIntoModel("CALL lab_user_by_id(?)", model, id);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000;
        model.addAttribute("elapsedMs", elapsedMs);
        return "lab/sql_time";
    }

    @GetMapping("/lab/xss/reflected")
    public String reflectedXss(@RequestParam(value = "q", defaultValue = "") String q, Model model) {
        model.addAttribute("q", q);
        return "lab/xss_reflected";
    }

    @GetMapping("/lab/xss/stored")
    public String storedXss(Model model) {
        try {
            List<Map<String, Object>> messages = jdbcTemplate.queryForList(
                    "SELECT id, author, content, created_at FROM lab_messages ORDER BY id DESC LIMIT 50");
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            model.addAttribute("dbError", rootMessage(e));
        }
        return "lab/xss_stored";
    }

    @PostMapping("/lab/xss/stored")
    public String submitStoredXss(@RequestParam(value = "author", defaultValue = "anonymous") String author,
            @RequestParam("content") String content) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/lab/xss/stored?error=empty";
        }
        jdbcTemplate.update("INSERT INTO lab_messages (author, content) VALUES (?, ?)", author, content);
        return "redirect:/lab/xss/stored?posted=1";
    }

    @GetMapping("/lab/discovery")
    public String discovery() {
        return "lab/discovery";
    }

    private void runIntoModel(String sql, Model model, Object... args) {
        try {
            QueryTable table = queryTable(sql, args);
            model.addAttribute("columns", table.columns());
            model.addAttribute("rows", table.rows());
            model.addAttribute("rowCount", table.rows().size());
            model.addAttribute("dbError", null);
        } catch (Exception e) {
            model.addAttribute("columns", List.of());
            model.addAttribute("rows", List.of());
            model.addAttribute("rowCount", 0);
            model.addAttribute("dbError", rootMessage(e));
        }
    }

    private QueryTable queryTable(String sql, Object... args) {
        return jdbcTemplate.query(sql, rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columns = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnLabel(i));
            }
            List<List<Object>> rows = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }
            return new QueryTable(columns, rows);
        }, args);
    }

    private String rootMessage(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage() != null ? cause.getMessage() : cause.toString();
    }

    private record QueryTable(List<String> columns, List<List<Object>> rows) {
    }
}
