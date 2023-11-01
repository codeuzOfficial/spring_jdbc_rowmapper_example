package dasturlash.uz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<StudentDTO> getStudentList() {
        String sql = "select id,name,surname, creatd_date from Student";
        List<StudentDTO> studentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(StudentDTO.class));
        return studentList;
    }


    public List<StudentDTO> getAllStudentListWithCustomRowMapper() {
        String sql = "select id,name,surname, created_date from Student";

        RowMapper<StudentDTO> rowMapper = new RowMapper<StudentDTO>() {
            @Override
            public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                StudentDTO dto = new StudentDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setSurname(rs.getString("surname"));
                if (rs.getTimestamp("created_date") != null) {
                    dto.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                }
                return dto;
            }
        };

        List<StudentDTO> studentList = jdbcTemplate.query(sql, rowMapper);
        return studentList;
    }

    public void save(StudentDTO dto) {
        String sql = "INSERT INTO student (name,surname,created_date) values('%s','%s','%s')";
        sql = String.format(sql, dto.getName(), dto.getSurname(), dto.getCreatedDate());
        jdbcTemplate.update(sql);
    }
}
