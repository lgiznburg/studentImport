package ru.rsmu.studentimport.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import ru.rsmu.studentimport.model.*;
import ru.rsmu.studentimport.service.StudentContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author leonid.
 */
@Repository
@SuppressWarnings( "unchecked" )
public class CommonDao extends HibernateDaoSupport {

    @Autowired
    public void setPersistentResource( SessionFactory sessionFactory ) {
        super.setSessionFactory( sessionFactory );
    }

    public void saveEntity( Object object) {
        try {
            getHibernateTemplate().saveOrUpdate( object );
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public <T> T findEntity( Class<T> entityClass, long id  ) {
        return getHibernateTemplate().get( entityClass, id );
    }

    public List<SpecialSubject> findAllSubjects() {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( SpecialSubject.class );

        return criteria.list();
    }

    public List<Country> findAllCountries() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( Country.class )
                .list();
    }

    public FacultyProgram findFacultyProgram( Faculty faculty, Direction direction ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( FacultyProgram.class )
                .add( Restrictions.eq( "faculty", faculty.getId() ) )
                .add( Restrictions.eq( "direction", direction.getId() ) )
                .add( Restrictions.eq( "educationForm", 1 ) )
                .setMaxResults( 1 );
        return (FacultyProgram) criteria.uniqueResult();
    }

    public void saveAllInfo( StudentContext context ) {
        getHibernateTemplate().saveOrUpdate( context.getStudent() );
        getHibernateTemplate().saveOrUpdate( context.getDfs() );
        getHibernateTemplate().saveOrUpdate( context.getAddress() );
        getHibernateTemplate().saveOrUpdate( context.getIdDocument() );
        getHibernateTemplate().saveOrUpdate( context.getEduDocument() );
        if ( context.getBenefit() != null ) getHibernateTemplate().saveOrUpdate( context.getBenefit() );
        for ( StudentLanguage language : context.getLanguages() ) {
            getHibernateTemplate().saveOrUpdate( language );
        }
    }

    public List<FacultyProgram> findAllFacultyProgram() {
        return getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( FacultyProgram.class )
                .add( Restrictions.eq( "educationForm", 1 ) )
                .list();
    }

    public <T> List<T> findAllEntity( Class<T> entityClass ) {
        return getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( entityClass )
                .list();
    }

    public StudentDFS findDfs( String caseNumber ) {
        return (StudentDFS) getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( StudentDFS.class )
                .add( Restrictions.eq( "caseNumber", caseNumber ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public OrderInfo findOrderInfo( String orderNumber, Date orderDate, FacultyProgram facultyProgram ) {
        return (OrderInfo) getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( OrderInfo.class )
                .add( Restrictions.eq( "orderNumber", orderNumber ) )
                .add( Restrictions.eq( "orderDate", orderDate ) )
                .add( Restrictions.eq( "facultyProgram", facultyProgram ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public Recruitment findActiveRecruitment() {
        Calendar year = Calendar.getInstance();
        return (Recruitment) getSessionFactory().getCurrentSession().createCriteria( Recruitment.class )
                //.add( Restrictions.eq( "active", true ) )
                .add( Restrictions.eq( "year1", Integer.valueOf( year.get( Calendar.YEAR ) ).longValue() ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public IdDocument findPrevYearDocument( IdDocument idDocument, Student student ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( IdDocument.class )
                .add( Restrictions.eq( "type", idDocument.getType() ) )
                .add( Restrictions.eq( "seria", idDocument.getSeria() ) )
                .add( Restrictions.eq( "number", idDocument.getNumber() ) )
                .createAlias( "student", "student" )
                .add( Restrictions.eq( "student.lastName", student.getLastName() ) )
                .add( Restrictions.eq( "student.firstName", student.getFirstName() ) )
                .add( Restrictions.eq( "student.middleName", student.getMiddleName() ) )
                .add( Restrictions.eq( "student.birthDate", student.getBirthDate() ) )
                .setMaxResults( 1 );

        return (IdDocument) criteria.uniqueResult();
    }

    public Address findAddress( Student student ) {
        return (Address) getSessionFactory().getCurrentSession().createCriteria( Address.class )
                .add( Restrictions.eq( "student", student ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public EduDocument findEduDocument( Student student ) {
        return (EduDocument) getSessionFactory().getCurrentSession().createCriteria( EduDocument.class )
                .add( Restrictions.eq( "student", student ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public PersonBenefit findBenefit( Student student ) {
        return (PersonBenefit) getSessionFactory().getCurrentSession().createCriteria( PersonBenefit.class )
                .add( Restrictions.eq( "student", student ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }

    public StudentLanguage findStudentLanguage( Student student, ForeignLanguage language ) {
        return (StudentLanguage) getSessionFactory().getCurrentSession().createCriteria( StudentLanguage.class )
                .add( Restrictions.eq( "student", student ) )
                .add( Restrictions.eq( "language", language ) )
                .setMaxResults( 1 )
                .uniqueResult();
    }
}
