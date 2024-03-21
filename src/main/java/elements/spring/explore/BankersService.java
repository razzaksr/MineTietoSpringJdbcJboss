package elements.spring.explore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class BankersService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProcedureResponse storedOne(int employeeId){

        CallableStatementCreator creator=con->{
            CallableStatement statement= con.prepareCall("{call read_bankers_info(?,?,?)}");
            statement.setInt(1,employeeId);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.registerOutParameter(3, Types.VARCHAR);
            return statement;
        };

        Map<String, Object> info = jdbcTemplate.call(creator, Arrays.asList(new SqlParameter[]{
                new SqlParameter(Types.NUMERIC),
                new SqlOutParameter("employeeName", Types.VARCHAR),
                new SqlOutParameter("employeeInfo", Types.VARCHAR)}));

//        Map<String,String> received = new HashMap<>();
//
//        received.put("employeeName", (String) info.get("employeeName"));
//        received.put("employeeInfo", (String) info.get("employeeInfo"));

        ProcedureResponse procedureResponse=new ProcedureResponse();

        procedureResponse.setEmployeeName((String) info.get("employeeName"));
        procedureResponse.setEmployeeInfo((String) info.get("employeeInfo"));
        return procedureResponse;
    }

    public String deleteOne(int id){
        String information=listOne(id).get().getBankerName()+" has deleted";
        jdbcTemplate.update("delete from bankers where banker_id=?",new Object[]{id});
        return information;
    }

    public String updateOne(Bankers bankers){
        String information=bankers.getBankerId()+" has updated";
        jdbcTemplate.update("update bankers set banker_name=?, banker_passcode=? where banker_id=?",
                new Object[]{bankers.getBankerName(),bankers.getBankerPasscode(),bankers.getBankerId()});
        return information;
    }

    public List<Bankers> readNames(String name){
        return jdbcTemplate.query("select * from bankers where banker_name=?",new Object[]{name},new BankersMapper());
    }

    public String insertion(Bankers bankers){
        String information=bankers.getBankerName()+" access created";
        jdbcTemplate.update("insert into bankers values(bank_seq.nextval,?,?)",new Object[]{bankers.getBankerName(),bankers.getBankerPasscode()});
        return information;
    }

    public Optional<Bankers> listOne(int id){
        return Optional.of(jdbcTemplate.queryForObject("select * from bankers where banker_id=?",new Object[]{id},
                new BeanPropertyRowMapper<>(Bankers.class)));
    }

    public List<Bankers> listAll(){
        return jdbcTemplate.query("select * from bankers",new BankersMapper());
    }

    class BankersMapper implements RowMapper<Bankers> {

        @Override
        public Bankers mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bankers bankers=new Bankers();
            bankers.setBankerId(rs.getInt("banker_id"));
            bankers.setBankerName(rs.getString("banker_name"));
            bankers.setBankerPasscode(rs.getString("banker_passcode"));
            return bankers;
        }
    }
}