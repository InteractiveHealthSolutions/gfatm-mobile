package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig attributeEntityDaoConfig;
    private final DaoConfig attributeTypeEntityDaoConfig;
    private final DaoConfig testOrderEntityDaoConfig;
    private final DaoConfig testTypeEntityDaoConfig;

    private final AttributeEntityDao attributeEntityDao;
    private final AttributeTypeEntityDao attributeTypeEntityDao;
    private final TestOrderEntityDao testOrderEntityDao;
    private final TestTypeEntityDao testTypeEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        attributeEntityDaoConfig = daoConfigMap.get(AttributeEntityDao.class).clone();
        attributeEntityDaoConfig.initIdentityScope(type);

        attributeTypeEntityDaoConfig = daoConfigMap.get(AttributeTypeEntityDao.class).clone();
        attributeTypeEntityDaoConfig.initIdentityScope(type);

        testOrderEntityDaoConfig = daoConfigMap.get(TestOrderEntityDao.class).clone();
        testOrderEntityDaoConfig.initIdentityScope(type);

        testTypeEntityDaoConfig = daoConfigMap.get(TestTypeEntityDao.class).clone();
        testTypeEntityDaoConfig.initIdentityScope(type);

        attributeEntityDao = new AttributeEntityDao(attributeEntityDaoConfig, this);
        attributeTypeEntityDao = new AttributeTypeEntityDao(attributeTypeEntityDaoConfig, this);
        testOrderEntityDao = new TestOrderEntityDao(testOrderEntityDaoConfig, this);
        testTypeEntityDao = new TestTypeEntityDao(testTypeEntityDaoConfig, this);

        registerDao(AttributeEntity.class, attributeEntityDao);
        registerDao(AttributeTypeEntity.class, attributeTypeEntityDao);
        registerDao(TestOrderEntity.class, testOrderEntityDao);
        registerDao(TestTypeEntity.class, testTypeEntityDao);
    }
    
    public void clear() {
        attributeEntityDaoConfig.clearIdentityScope();
        attributeTypeEntityDaoConfig.clearIdentityScope();
        testOrderEntityDaoConfig.clearIdentityScope();
        testTypeEntityDaoConfig.clearIdentityScope();
    }

    public AttributeEntityDao getAttributeEntityDao() {
        return attributeEntityDao;
    }

    public AttributeTypeEntityDao getAttributeTypeEntityDao() {
        return attributeTypeEntityDao;
    }

    public TestOrderEntityDao getTestOrderEntityDao() {
        return testOrderEntityDao;
    }

    public TestTypeEntityDao getTestTypeEntityDao() {
        return testTypeEntityDao;
    }

}