package ma.enset.exam2test.DAO;

import java.sql.SQLException;
import java.sql.SQLException;
import java.util.List;

public interface DAO<E,U> {
    void create(E e) throws SQLException;
    void deleteById(U u) throws SQLException;
    List<E> findAll()  throws SQLException;
    E findByID(U u) throws SQLException;
    void update(E e)throws SQLException;


}