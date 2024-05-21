package com.travelog.util.handler;

import com.travelog.plan.dto.Transportation;
import com.travelog.plan.exception.WrongTransportationException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Transportation.class)
public class TransportationTypeHandler implements TypeHandler<Transportation> {

    @Override
    // 지정된 타입의 값을 DB에 어떻게 저장?
    public void setParameter(PreparedStatement ps, int i, Transportation parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i,parameter.getTransportation());
    }

    @Override
    // 컬럼 이름 기반으로 조회된 값을 활용해서 실제 반환할 객체 구성
    public Transportation getResult(ResultSet rs, String columnName) throws SQLException {
        int trValue = rs.getInt(columnName);
        return getTransportation(trValue);
    }

    @Override
    // 컬럼 index 기반으로 조회된 값을 활용해서 실제 반환할 객체 구성
    public Transportation getResult(ResultSet rs, int columnIndex) throws SQLException {
        int trValue = rs.getInt(columnIndex);
        return getTransportation(trValue);
    }

    @Override
    // Callablestatement에서 컬럼 index를 기반으로 조회된 값을 활용해서 실제 반환할 객체 구성
    public Transportation getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int trValue = cs.getInt(columnIndex);
        return getTransportation(trValue);
    }

    // 실제 객체를 구성하는 메서드
    private Transportation getTransportation(int transportation){
        Transportation tr = null;
        switch (transportation){
            case 1:
                tr = Transportation.CAR;
                break;
            case 2:
                tr = Transportation.WALK;
                break;
            case 3:
                tr= Transportation.PT;
                break;
            default:
                break;
        }
        return tr;
    }


}
