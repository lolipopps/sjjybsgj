package com.sjjybsgj.standard_db.mapper;

import com.sjjybsgj.standard_db.model.StandardDb;
import com.sjjybsgj.standard_db.model.StandardDbExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StandardDbMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    long countByExample(StandardDbExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int deleteByExample(StandardDbExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int deleteByPrimaryKey(String dbRuleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int insert(StandardDb record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int insertSelective(StandardDb record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    List<StandardDb> selectByExample(StandardDbExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    StandardDb selectByPrimaryKey(String dbRuleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int updateByExampleSelective(@Param("record") StandardDb record, @Param("example") StandardDbExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int updateByExample(@Param("record") StandardDb record, @Param("example") StandardDbExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int updateByPrimaryKeySelective(StandardDb record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table standard_db
     *
     * @mbg.generated Sun May 27 19:07:39 CST 2018
     */
    int updateByPrimaryKey(StandardDb record);
}