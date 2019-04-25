package com.supcon.orchid.BEAM.services.impl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map; 
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.supcon.orchid.audit.annotation.AuditLog;
import com.supcon.orchid.audit.entities.AuditUtil;
import com.supcon.orchid.id.IdGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import java.io.Serializable;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.util.HtmlUtils;
import com.supcon.orchid.workflow.engine.transformers.PendingResultTransformer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import com.supcon.orchid.services.BAPException;
import com.supcon.orchid.utils.DateUtils;
import com.supcon.orchid.services.BaseServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.supcon.orchid.security.OrchidAuthenticationToken;

import com.supcon.orchid.foundation.services.ACLPermissionHandleService;
import com.supcon.orchid.services.IModelTreeLayRecService;
import com.supcon.orchid.container.mvc.struts2.results.BAPEntityTransformer;
import com.supcon.orchid.container.mvc.struts2.utils.Struts2Utils;
import com.supcon.orchid.container.mvc.struts2.utils.XmlUtils;
import com.supcon.orchid.counter.Counter;
import com.supcon.orchid.counter.CounterManager;
import com.supcon.orchid.counter.CounterType;
import com.supcon.orchid.counter.support.NonCachedCounterBean;
import com.supcon.orchid.BEAM.entities.BEAMBaseInfoMneCode;
import com.supcon.orchid.BEAM.services.BEAMBaseInfoService;
import com.supcon.orchid.BEAM.services.BEAMBaseInfoImportService;
import com.supcon.orchid.BEAM.entities.BEAMBaseInfoDealInfo;
import com.supcon.orchid.utils.BAPEventPublisher;
import com.supcon.orchid.utils.DateUtils;
import com.supcon.orchid.utils.Inflector;
import com.supcon.orchid.utils.OrchidUtils;
import com.supcon.orchid.utils.Param;
import com.supcon.orchid.foundation.entities.Document;
import com.supcon.orchid.foundation.entities.Staff;
import com.supcon.orchid.foundation.entities.SystemCode;
import com.supcon.orchid.foundation.entities.User;
import com.supcon.orchid.db.DbUtils;
import com.supcon.orchid.utils.JSONPlainSerializer;
import com.supcon.orchid.ec.entities.AdvQueryCondition;
import com.supcon.orchid.ec.entities.CustomPropertyModelMapping;
import com.supcon.orchid.ec.entities.CustomPropertyViewMapping;
import com.supcon.orchid.ec.entities.Sql;
import com.supcon.orchid.ec.entities.View;
import com.supcon.orchid.ec.entities.DataGrid;
import com.supcon.orchid.ec.entities.DealInfo;
import com.supcon.orchid.ec.entities.Event;
import com.supcon.orchid.ec.entities.ExtraView;
import com.supcon.orchid.ec.services.ConditionService;
import com.supcon.orchid.ec.services.SqlService;
import com.supcon.orchid.ec.services.ViewServiceFoundation;
import com.supcon.orchid.ec.services.EventService;
import com.supcon.orchid.ec.services.DataGridService;
import com.supcon.orchid.utils.ReflectUtils;
import com.supcon.orchid.ec.services.CreatorService;
import com.supcon.orchid.foundation.services.DataPermissionService;
import com.supcon.orchid.foundation.services.DocumentService;
import com.supcon.orchid.foundation.services.SynchronizeInfoService;
import com.supcon.orchid.script.entities.Script;
import com.supcon.orchid.script.EngineScriptExecutor;
import com.supcon.orchid.script.services.ScriptService;
import com.supcon.orchid.i18n.InternationalResource;
import com.supcon.orchid.orm.dao.BaseDao.DBTYPE;
import com.supcon.orchid.orm.enums.DealInfoType;
import com.supcon.orchid.ec.entities.EntityTableInfo;
import com.supcon.orchid.ec.enums.ViewType;
import com.supcon.orchid.ec.enums.ShowType;
import com.supcon.orchid.BEAM.entities.BEAMAuthorityDepartment;
import com.supcon.orchid.BEAM.daos.BEAMAuthorityDepartmentDao;
import com.supcon.orchid.BEAM.services.BEAMAuthorityDepartmentService;
import com.supcon.orchid.BEAM.entities.BEAMPowerArea;
import com.supcon.orchid.BEAM.daos.BEAMPowerAreaDao;
import com.supcon.orchid.BEAM.services.BEAMPowerAreaService;
import com.supcon.orchid.BEAM.entities.BEAMPowerDept;
import com.supcon.orchid.BEAM.daos.BEAMPowerDeptDao;
import com.supcon.orchid.BEAM.services.BEAMPowerDeptService;
import com.supcon.orchid.BEAM.entities.BEAMPowerDutyStaff;
import com.supcon.orchid.BEAM.daos.BEAMPowerDutyStaffDao;
import com.supcon.orchid.BEAM.services.BEAMPowerDutyStaffService;
import com.supcon.orchid.BEAM.entities.BEAMPowerEAM;
import com.supcon.orchid.BEAM.daos.BEAMPowerEAMDao;
import com.supcon.orchid.BEAM.services.BEAMPowerEAMService;
import com.supcon.orchid.BEAM.entities.BEAMPowerEamType;
import com.supcon.orchid.BEAM.daos.BEAMPowerEamTypeDao;
import com.supcon.orchid.BEAM.services.BEAMPowerEamTypeService;
import com.supcon.orchid.BEAM.entities.BEAMPowerHead;
import com.supcon.orchid.BEAM.daos.BEAMPowerHeadDao;
import com.supcon.orchid.BEAM.services.BEAMPowerHeadService;
import com.supcon.orchid.BEAM.entities.BEAMPowerRole;
import com.supcon.orchid.BEAM.daos.BEAMPowerRoleDao;
import com.supcon.orchid.BEAM.services.BEAMPowerRoleService;
import com.supcon.orchid.BEAM.entities.BEAMPowerStaff;
import com.supcon.orchid.BEAM.daos.BEAMPowerStaffDao;
import com.supcon.orchid.BEAM.services.BEAMPowerStaffService;
import com.supcon.orchid.BEAM.entities.BEAMPowerType;
import com.supcon.orchid.BEAM.daos.BEAMPowerTypeDao;
import com.supcon.orchid.BEAM.services.BEAMPowerTypeService;
import com.supcon.orchid.BEAM.entities.BEAMLubricatingPart;
import com.supcon.orchid.BEAM.daos.BEAMLubricatingPartDao;
import com.supcon.orchid.BEAM.services.BEAMLubricatingPartService;
import com.supcon.orchid.BEAM.entities.BEAMAttachPart;
import com.supcon.orchid.BEAM.daos.BEAMAttachPartDao;
import com.supcon.orchid.BEAM.services.BEAMAttachPartService;
import com.supcon.orchid.BEAM.entities.BEAMBaseCheck;
import com.supcon.orchid.BEAM.daos.BEAMBaseCheckDao;
import com.supcon.orchid.BEAM.services.BEAMBaseCheckService;
import com.supcon.orchid.BEAM.entities.BEAMBaseCheckItem;
import com.supcon.orchid.BEAM.daos.BEAMBaseCheckItemDao;
import com.supcon.orchid.BEAM.services.BEAMBaseCheckItemService;
import com.supcon.orchid.BEAM.entities.BEAMBaseInfo;
import com.supcon.orchid.BEAM.daos.BEAMBaseInfoDao;
import com.supcon.orchid.BEAM.services.BEAMBaseInfoService;
import com.supcon.orchid.BEAM.entities.BEAMBaseinfoParam;
import com.supcon.orchid.BEAM.daos.BEAMBaseinfoParamDao;
import com.supcon.orchid.BEAM.services.BEAMBaseinfoParamService;
import com.supcon.orchid.BEAM.entities.BEAMDealInfoTable;
import com.supcon.orchid.BEAM.daos.BEAMDealInfoTableDao;
import com.supcon.orchid.BEAM.services.BEAMDealInfoTableService;
import com.supcon.orchid.BEAM.entities.BEAMDelayRecords;
import com.supcon.orchid.BEAM.daos.BEAMDelayRecordsDao;
import com.supcon.orchid.BEAM.services.BEAMDelayRecordsService;
import com.supcon.orchid.BEAM.entities.BEAMDocPart;
import com.supcon.orchid.BEAM.daos.BEAMDocPartDao;
import com.supcon.orchid.BEAM.services.BEAMDocPartService;
import com.supcon.orchid.BEAM.entities.BEAMJWXItem;
import com.supcon.orchid.BEAM.daos.BEAMJWXItemDao;
import com.supcon.orchid.BEAM.services.BEAMJWXItemService;
import com.supcon.orchid.BEAM.entities.BEAMJwxRuleBeam;
import com.supcon.orchid.BEAM.daos.BEAMJwxRuleBeamDao;
import com.supcon.orchid.BEAM.services.BEAMJwxRuleBeamService;
import com.supcon.orchid.BEAM.entities.BEAMJwxRuleHead;
import com.supcon.orchid.BEAM.daos.BEAMJwxRuleHeadDao;
import com.supcon.orchid.BEAM.services.BEAMJwxRuleHeadService;
import com.supcon.orchid.BEAM.entities.BEAMReportInfo;
import com.supcon.orchid.BEAM.daos.BEAMReportInfoDao;
import com.supcon.orchid.BEAM.services.BEAMReportInfoService;
import com.supcon.orchid.BEAM.entities.BEAMSparePart;
import com.supcon.orchid.BEAM.daos.BEAMSparePartDao;
import com.supcon.orchid.BEAM.services.BEAMSparePartService;
import com.supcon.orchid.BEAM.entities.BEAMSpareRecord;
import com.supcon.orchid.BEAM.daos.BEAMSpareRecordDao;
import com.supcon.orchid.BEAM.services.BEAMSpareRecordService;
import com.supcon.orchid.BEAM.entities.BEAMWorkRecord;
import com.supcon.orchid.BEAM.daos.BEAMWorkRecordDao;
import com.supcon.orchid.BEAM.services.BEAMWorkRecordService;
import com.supcon.orchid.BEAM.entities.BEAMCandidateValue;
import com.supcon.orchid.BEAM.daos.BEAMCandidateValueDao;
import com.supcon.orchid.BEAM.services.BEAMCandidateValueService;
import com.supcon.orchid.BEAM.entities.BEAMInputStandard;
import com.supcon.orchid.BEAM.daos.BEAMInputStandardDao;
import com.supcon.orchid.BEAM.services.BEAMInputStandardService;
import com.supcon.orchid.BEAM.entities.BEAMGroupComponent;
import com.supcon.orchid.BEAM.daos.BEAMGroupComponentDao;
import com.supcon.orchid.BEAM.services.BEAMGroupComponentService;
import com.supcon.orchid.BEAM.entities.BEAMRepairGroup;
import com.supcon.orchid.BEAM.daos.BEAMRepairGroupDao;
import com.supcon.orchid.BEAM.services.BEAMRepairGroupService;
import com.supcon.orchid.BEAM.entities.BEAMChange;
import com.supcon.orchid.BEAM.daos.BEAMChangeDao;
import com.supcon.orchid.BEAM.services.BEAMChangeService;
import com.supcon.orchid.BEAM.entities.BEAMChangePart;
import com.supcon.orchid.BEAM.daos.BEAMChangePartDao;
import com.supcon.orchid.BEAM.services.BEAMChangePartService;
import com.supcon.orchid.BEAM.entities.BEAMCustomPropertyModel;
import com.supcon.orchid.BEAM.daos.BEAMCustomPropertyModelDao;
import com.supcon.orchid.BEAM.services.BEAMCustomPropertyModelService;
import com.supcon.orchid.BEAM.entities.BEAMEamType;
import com.supcon.orchid.BEAM.daos.BEAMEamTypeDao;
import com.supcon.orchid.BEAM.services.BEAMEamTypeService;
import com.supcon.orchid.BEAM.entities.BEAMLubricateOil;
import com.supcon.orchid.BEAM.daos.BEAMLubricateOilDao;
import com.supcon.orchid.BEAM.services.BEAMLubricateOilService;
import com.supcon.orchid.BEAM.entities.BEAMBusitype;
import com.supcon.orchid.BEAM.daos.BEAMBusitypeDao;
import com.supcon.orchid.BEAM.services.BEAMBusitypeService;
import com.supcon.orchid.BEAM.entities.BEAMBaseInfoWorkflow;
import com.supcon.orchid.BEAM.daos.BEAMBaseInfoWorkflowDao;
import com.supcon.orchid.BEAM.services.BEAMBaseInfoWorkflowService;
import com.supcon.orchid.BEAM.entities.BEAMUpdateInfo;
import com.supcon.orchid.BEAM.daos.BEAMUpdateInfoDao;
import com.supcon.orchid.BEAM.services.BEAMUpdateInfoService;
import com.supcon.orchid.BEAM.entities.BEAMArea;
import com.supcon.orchid.BEAM.daos.BEAMAreaDao;
import com.supcon.orchid.BEAM.services.BEAMAreaService;
import com.supcon.orchid.services.Page;
import com.supcon.orchid.BEAM.entities.BEAMBaseCheck;
import com.supcon.orchid.foundation.entities.Staff;
import com.supcon.orchid.BEAM.entities.BEAMBusitype;
import com.supcon.orchid.foundation.entities.Department;
import com.supcon.orchid.BEAM.entities.BEAMJwxRuleHead;
import com.supcon.orchid.foundation.entities.Position;
import com.supcon.orchid.tree.PrepareData;
import com.supcon.orchid.tree.Tree;
import com.supcon.orchid.tree.TreeDao;
import javax.annotation.Resource;
import com.supcon.orchid.ec.entities.Field;
import com.supcon.orchid.ec.entities.Model;
import com.supcon.orchid.ec.entities.Property;
import com.supcon.orchid.ec.services.IBAPBaseService;
import com.supcon.orchid.ec.services.ModelServiceFoundation;
import com.supcon.orchid.ec.enums.RegionType;
import com.supcon.orchid.utils.SerializeUitls;
import com.supcon.orchid.ec.services.EcConfigService;
import com.supcon.orchid.ec.services.FieldService;
import com.supcon.orchid.ec.enums.DbColumnType;
import com.supcon.orchid.workflow.engine.transformers.PendingResultTransformer;
import com.supcon.orchid.BEAM.daos.impl.BEAMBaseInfoDaoImpl;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;
import com.supcon.orchid.annotation.BAPIsMneCode;
import com.supcon.orchid.annotation.BAPMneField;
import javax.persistence.Table;
import com.supcon.orchid.utils.MneCodeGenterate;
import com.supcon.orchid.audit.service.DataAuditLogService;
import com.supcon.orchid.audit.entities.SignatureLog;
import com.supcon.orchid.workflow.engine.services.TransitionService;
import com.supcon.orchid.foundation.services.ReliableMessageSenderService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
/* CUSTOM CODE START(serviceimpl,import,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码
import com.supcon.orchid.services.ConsulService;
import com.supcon.orchid.BEAM.services.BEAMWorkJWXService;
import java.sql.DriverManager;
import java.sql.Connection;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.BufferedInputStream;
import javax.persistence.Table;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.JDBCException;
import com.supcon.orchid.foundation.services.DepartmentService;
import com.supcon.orchid.foundation.entities.Department;
import com.supcon.orchid.MESBasic.services.MobileApiService;
import org.osgi.framework.Bundle;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import com.supcon.orchid.BEAM.entities.BEAMChangeInfo;
import com.supcon.orchid.BEAM.daos.BEAMChangeInfoDao;
/* CUSTOM CODE END */

@Service("bEAM_baseInfoService")
@Transactional
public class BEAMBaseInfoServiceImpl  extends BaseServiceImpl<BEAMBaseInfo> implements BEAMBaseInfoService, InitializingBean, DisposableBean {
	private static final Pattern pattern = Pattern.compile("( AS | as )((\"?)(.*?)(\"?))(,| )");
	private static final Pattern p = Pattern.compile("( JOIN | join )((.*?)) ((\"?)(.*?)(\"?))( ON | on )");
	private static final Pattern countPattern = Pattern.compile("(SUM\\()((\"?)(.*?)(\"?))(\\) AS | as )");
	@Autowired
	ReliableMessageSenderService  reliableMessageSenderService;//消息发送服务
    @Autowired
	private ACLPermissionHandleService aclPermissionService;
	@Autowired
	private SqlService sqlService;
	@Autowired
	EventService eventService;
	@Autowired
	ScriptService scriptService;
	@Autowired
	private IdGenerator autoGeneratorID;
	@Autowired
	private DataAuditLogService dataAuditLogService;
	@Value("${bap.company.single}")
	private Boolean isSingleMode = false;
	
	public Boolean getIsSingleMode() {
		if(null == isSingleMode)
			isSingleMode = false;
		return isSingleMode;
	}
	@Autowired
	private DocumentService documentService;
	@Autowired
	private BEAMAuthorityDepartmentDao authorityDepartmentDao;
	@Autowired
	private BEAMAuthorityDepartmentService authorityDepartmentService;
	@Autowired
	private BEAMPowerAreaDao powerAreaDao;
	@Autowired
	private BEAMPowerAreaService powerAreaService;
	@Autowired
	private BEAMPowerDeptDao powerDeptDao;
	@Autowired
	private BEAMPowerDeptService powerDeptService;
	@Autowired
	private BEAMPowerDutyStaffDao powerDutyStaffDao;
	@Autowired
	private BEAMPowerDutyStaffService powerDutyStaffService;
	@Autowired
	private BEAMPowerEAMDao powerEAMDao;
	@Autowired
	private BEAMPowerEAMService powerEAMService;
	@Autowired
	private BEAMPowerEamTypeDao powerEamTypeDao;
	@Autowired
	private BEAMPowerEamTypeService powerEamTypeService;
	@Autowired
	private BEAMPowerHeadDao powerHeadDao;
	@Autowired
	private BEAMPowerHeadService powerHeadService;
	@Autowired
	private BEAMPowerRoleDao powerRoleDao;
	@Autowired
	private BEAMPowerRoleService powerRoleService;
	@Autowired
	private BEAMPowerStaffDao powerStaffDao;
	@Autowired
	private BEAMPowerStaffService powerStaffService;
	@Autowired
	private BEAMPowerTypeDao powerTypeDao;
	@Autowired
	private BEAMPowerTypeService powerTypeService;
	@Autowired
	private BEAMLubricatingPartDao lubricatingPartDao;
	@Autowired
	private BEAMLubricatingPartService lubricatingPartService;
	@Autowired
	private BEAMAttachPartDao attachPartDao;
	@Autowired
	private BEAMAttachPartService attachPartService;
	@Autowired
	private BEAMBaseCheckDao baseCheckDao;
	@Autowired
	private BEAMBaseCheckService baseCheckService;
	@Autowired
	private BEAMBaseCheckItemDao baseCheckItemDao;
	@Autowired
	private BEAMBaseCheckItemService baseCheckItemService;
	@Autowired
	private BEAMBaseInfoDao baseInfoDao;
	@Autowired
	private BEAMBaseinfoParamDao baseinfoParamDao;
	@Autowired
	private BEAMBaseinfoParamService baseinfoParamService;
	@Autowired
	private BEAMDealInfoTableDao dealInfoTableDao;
	@Autowired
	private BEAMDealInfoTableService dealInfoTableService;
	@Autowired
	private BEAMDelayRecordsDao delayRecordsDao;
	@Autowired
	private BEAMDelayRecordsService delayRecordsService;
	@Autowired
	private BEAMDocPartDao docPartDao;
	@Autowired
	private BEAMDocPartService docPartService;
	@Autowired
	private BEAMJWXItemDao jWXItemDao;
	@Autowired
	private BEAMJWXItemService jWXItemService;
	@Autowired
	private BEAMJwxRuleBeamDao jwxRuleBeamDao;
	@Autowired
	private BEAMJwxRuleBeamService jwxRuleBeamService;
	@Autowired
	private BEAMJwxRuleHeadDao jwxRuleHeadDao;
	@Autowired
	private BEAMJwxRuleHeadService jwxRuleHeadService;
	@Autowired
	private BEAMReportInfoDao reportInfoDao;
	@Autowired
	private BEAMReportInfoService reportInfoService;
	@Autowired
	private BEAMSparePartDao sparePartDao;
	@Autowired
	private BEAMSparePartService sparePartService;
	@Autowired
	private BEAMSpareRecordDao spareRecordDao;
	@Autowired
	private BEAMSpareRecordService spareRecordService;
	@Autowired
	private BEAMWorkRecordDao workRecordDao;
	@Autowired
	private BEAMWorkRecordService workRecordService;
	@Autowired
	private BEAMCandidateValueDao candidateValueDao;
	@Autowired
	private BEAMCandidateValueService candidateValueService;
	@Autowired
	private BEAMInputStandardDao inputStandardDao;
	@Autowired
	private BEAMInputStandardService inputStandardService;
	@Autowired
	private BEAMGroupComponentDao groupComponentDao;
	@Autowired
	private BEAMGroupComponentService groupComponentService;
	@Autowired
	private BEAMRepairGroupDao repairGroupDao;
	@Autowired
	private BEAMRepairGroupService repairGroupService;
	@Autowired
	private BEAMChangeDao changeDao;
	@Autowired
	private BEAMChangeService changeService;
	@Autowired
	private BEAMChangePartDao changePartDao;
	@Autowired
	private BEAMChangePartService changePartService;
	@Autowired
	private BEAMCustomPropertyModelDao customPropertyModelDao;
	@Autowired
	private BEAMCustomPropertyModelService customPropertyModelService;
	@Autowired
	private BEAMEamTypeDao eamTypeDao;
	@Autowired
	private BEAMEamTypeService eamTypeService;
	@Autowired
	private BEAMLubricateOilDao lubricateOilDao;
	@Autowired
	private BEAMLubricateOilService lubricateOilService;
	@Autowired
	private BEAMBusitypeDao busitypeDao;
	@Autowired
	private BEAMBusitypeService busitypeService;
	@Autowired
	private BEAMBaseInfoWorkflowDao baseInfoWorkflowDao;
	@Autowired
	private BEAMBaseInfoWorkflowService baseInfoWorkflowService;
	@Autowired
	private BEAMUpdateInfoDao updateInfoDao;
	@Autowired
	private BEAMUpdateInfoService updateInfoService;
	@Autowired
	private BEAMAreaDao areaDao;
	@Autowired
	private BEAMAreaService areaService;
	
	@Autowired
	private DataPermissionService dataPermissionService;
	@Autowired
	private CounterManager counterManager;
	@Autowired
	private ConditionService conditionService;
	@Autowired
	private ViewServiceFoundation viewServiceFoundation;
	private Counter counter;
	private Counter codeCounter;
	@Autowired
	private CreatorService creatorService;
	@Autowired
	private BundleContext bundleContext;
	@Autowired
	private SynchronizeInfoService synchronizeInfoService;
	@Autowired
	private ModelServiceFoundation modelServiceFoundation;
	@Resource
	private IBAPBaseService<BEAMBaseInfo> bapBaseService;
	
	@Autowired
	private FieldService fieldService;
	@Autowired
	private EcConfigService ecConfigService;
	@Autowired
	private BEAMBaseInfoImportService baseInfoImportService;
	/**
	 * 根据主显示字段列表获取Map
	 * @param mainDisplayKeys 主显示字段
	 * @return Map<String,String> key：count  value：数据数量；  key：idMap  value：id
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Map<String, Object> getMainDisplayMap(Serializable mainDisplayName, Serializable businessKeyName,List<Serializable> mainDisplayKeys) {
		String hql = "select " + mainDisplayName + ",id,"+ businessKeyName + " from " + BEAMBaseInfo.JPA_NAME + " where " + mainDisplayName + " in (:mainDisplayKeys)"  + "and VALID = 1";
		Query query =  baseInfoDao.createQuery(hql);
		query.setParameterList("mainDisplayKeys", mainDisplayKeys);
		List<Object[]> list = query.list();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("count", list.size());
		Map<Serializable, Serializable> map = new HashMap<Serializable, Serializable>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objs : list) {
				map.put((Serializable) (String.valueOf(objs[0])), (Serializable) objs[1]);
				map.put(businessKeyName, (Serializable) objs[2]);
			}
		}
		m.put("idMap", map);
		return m;
	}
	/**
	 * 根据业务主键列表获取Map
	 * @param businessKeys 业务主键列表
	 * @return Map<String,String> key：businessKey  value：id
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Map<Serializable,Serializable> getBusinessKeyMap(Serializable businessKeyName,List<Serializable> businessKeys){
		if(businessKeyName!=null && String.valueOf(businessKeyName).length() > 0){
			String hql = "select " + businessKeyName + ",id from " + BEAMBaseInfo.JPA_NAME + " where " + businessKeyName + " in (:businessKeys)";
			Query query = baseInfoDao.createQuery(hql);
			query.setParameterList("businessKeys", businessKeys);
			List<Object[]> list = query.list();
			Map<Serializable,Serializable> map = new HashMap<Serializable,Serializable>();
			if(list!=null && !list.isEmpty()){
				for (Object[] objs : list) {
					map.put((Serializable)(String.valueOf(objs[0])), (Serializable)objs[1]);
				}
			}
			return map;
		}
		return null;	
		
	}
	
	/**
	 * 获取父节点对象
	*/
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Map<String, Object> getParentNodeMap(Serializable mainDisplayName, Serializable businessKeyName, List<Serializable> serial, String queryParam) {
		String hql = null;
		if(queryParam!=null && queryParam.equals("md")){
			hql = "select " + mainDisplayName + ",id,"+ businessKeyName + ",fullPathName,layNo,layRec,parentId,leaf from " + BEAMBaseInfo.JPA_NAME + " where " + mainDisplayName + " in (:serial)";
		}else if(queryParam!=null && queryParam.equals("bk")){
			hql = "select " + mainDisplayName + ",id,"+ businessKeyName + ",fullPathName,layNo,layRec,parentId,leaf from " + BEAMBaseInfo.JPA_NAME + " where " + businessKeyName + " in (:serial)";
		}
		Query query =  baseInfoDao.createQuery(hql);
		query.setParameterList("serial", serial);
		List<Object[]> list = query.list();
		/*String sql = null;
		if(queryParam!=null && queryParam.equals("md")){
			sql = "select * from " + BEAMBaseInfo.TABLE_NAME + " where " + mainDisplayName + " = ?";
		}else if(queryParam!=null && queryParam.equals("bk")){
			sql = "select * from " + BEAMBaseInfo.TABLE_NAME + " where " + businessKeyName + " = ?";
		}
		List<BEAMBaseInfo> list = (List<BEAMBaseInfo>) baseInfoDao.createNativeQuery(sql,serial.get(0)).list();
		*/
		Map<String, Object> m = new HashMap<String, Object>();
		if(queryParam!=null && queryParam.equals("md")){
			m.put("count", list.size());
		}
		if (list != null && !list.isEmpty()) {
			for (Object[] obj : list) {
				m.put(serial.get(0).toString(), obj);
			}
		}
		
		return m;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BEAMBaseInfo getBaseInfo(long id){
		return getBaseInfo(id, null);
	}

	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BEAMBaseInfo getBaseInfo(long id, String viewCode){
		BEAMBaseInfo baseInfo = baseInfoDao.load(id);

		// 一对多情况处理
		if(baseInfo != null){
			this.setEamIDStaffID(baseInfo, viewCode);
			this.setEamIDBusiType(baseInfo, viewCode);
			this.setEamIDDeptID(baseInfo, viewCode);
			this.setEamIDJwxRuleHead(baseInfo, viewCode);
			this.setEamIDPositionID(baseInfo, viewCode);
		}
		return baseInfo;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String getBaseInfoAsJSON(long id, String include){
		BEAMBaseInfo baseInfo = baseInfoDao.load(id);
		// 一对多情况处理
			this.setEamIDStaffID(baseInfo);
			this.setEamIDBusiType(baseInfo);
			this.setEamIDDeptID(baseInfo);
			this.setEamIDJwxRuleHead(baseInfo);
			this.setEamIDPositionID(baseInfo);

		if(baseInfo == null) {
			return "";
		}
		return JSONPlainSerializer.serializeAsJSON(baseInfo, include, new BAPEntityTransformer());
	}
	

	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(BEAMBaseInfo baseInfo){
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		beforeDeleteBaseInfo(baseInfo);
		baseInfoDao.delete(baseInfo);
		afterDeleteBaseInfo(baseInfo);
		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/del", baseInfo,BEAMBaseInfoMneCode.class);
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		params.add(baseInfo);
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		props.put("eventType", "delete");
		String delIds = "";
		// 一对多情况处理
		delIds = baseInfo.getEamIDStaffIDmultiselectIDs();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("staffID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		delIds = baseInfo.getEamIDBusiTypemultiselectIDs();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("busiType.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		delIds = baseInfo.getEamIDDeptIDmultiselectIDs();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("deptID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		delIds = baseInfo.getEamIDJwxRuleHeadmultiselectIDs();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("jwxRuleHead.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		delIds = baseInfo.getEamIDPositionIDmultiselectIDs();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("positionID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
			// TODO delete
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(List<Long> baseInfoIds){
		deleteBaseInfo(baseInfoIds, null);
	}

	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(List<Long> baseInfoIds, String eventTopic) {
		List<BEAMBaseInfo> baseInfos = new ArrayList<BEAMBaseInfo>();
		for(Long baseInfoId : baseInfoIds){
			BEAMBaseInfo baseInfo = getBaseInfo(baseInfoId);
			baseInfos.add(baseInfo);
			if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
				if(!StringUtils.isEmpty(AuditUtil.getColumnStringA())){
					AuditUtil.setColumnStringA(AuditUtil.getColumnStringA() + (null == baseInfo.getName() ? "" : "," + baseInfo.getName().toString()));
				} else {
					AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
				}
				if(!StringUtils.isEmpty(AuditUtil.getColumnStringB())){
					AuditUtil.setColumnStringB(AuditUtil.getColumnStringB() + (null == baseInfo.getCode() ? "" : "," + baseInfo.getCode().toString()));
				} else {
					AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
				}
				if(!StringUtils.isEmpty(AuditUtil.getColumnStringC())){
					AuditUtil.setColumnStringC(AuditUtil.getColumnStringC() + (null == baseInfo.getId() ? "" : "," + baseInfo.getId().toString()));
				} else {
					AuditUtil.setColumnStringC(null == baseInfo.getId() ? "" : baseInfo.getId().toString());
				}
			}
		}
		
		for(BEAMBaseInfo baseInfo : baseInfos){
			beforeDeleteBaseInfo(baseInfo);
		}
		
		
		/**
		 * 假删
		 * 增加SQL/HQL执行的数据日志记录
		 */
		if(baseInfos != null && baseInfos.size() > 0 ){
			for(BEAMBaseInfo baseInfo : baseInfos){
				List<String> propertyNames = new ArrayList<String>();
				List<Object> previousState = new ArrayList<Object>();
				List<Object> currentState = new ArrayList<Object>();
				propertyNames.add("valid");
				currentState.add(false);
				previousState.add(true);
				dataAuditLogService.saveCustomerAudit("删除",baseInfo, baseInfo.getId(), currentState.toArray(), previousState.toArray(), propertyNames.toArray(new String[propertyNames.size()]));
			}
		}
		if(baseInfoIds != null && baseInfoIds.size() > 0) {
			String hql = "update " + BEAMBaseInfo.JPA_NAME + " set valid = false where id in(:ids)";
			Query query = baseInfoDao.createQuery(hql);
			query.setParameterList("ids", baseInfoIds);
			query.executeUpdate();
		}
		
		for(BEAMBaseInfo baseInfo : baseInfos){
			afterDeleteBaseInfo(baseInfo);
		}
		
		if(eventTopic==null){
			for(BEAMBaseInfo baseInfoz : baseInfos){
				baseInfoz.setValid(false);
				List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
				List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfoz));
				baseInfoz.setBaseCheckList(baseCheckList);
				List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfoz));
				baseInfoz.setBaseinfoParamList(baseinfoParamList);
				params.add(baseInfoz);
				Map<String, Object> props = new HashMap<String, Object>();
				props.put("callType", "service");
				props.put("entityCode", "BEAM_1.0.0_baseInfo");
				props.put("eventType", "delete");
			}
		}
	}

	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(long baseInfoId, int baseInfoVersion){
		this.deleteBaseInfo(baseInfoId, baseInfoVersion,null);
	}
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(long baseInfoId, int baseInfoVersion,SignatureLog signatureLog){
		BEAMBaseInfo baseInfo = getBaseInfo(baseInfoId);
		if(baseInfo != null && !baseInfo.isValid()){
			throw new BAPException(BAPException.Code.OBJECT_HAVE_BEAN_DELETED);
		}
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		beforeDeleteBaseInfo(baseInfo);
		baseInfoDao.delete(baseInfoId, baseInfoVersion);
		afterDeleteBaseInfo(baseInfo);
		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/del", baseInfo,BEAMBaseInfoMneCode.class);
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		params.add(baseInfo);
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		props.put("eventType", "delete");
		
		if(signatureLog != null ) {
			Object businessKey=null;
			businessKey=baseInfo.getCode();
			signatureLog.setTableId(baseInfo.getId());
			if(businessKey != null) {
				if(StringUtils.isEmpty(signatureLog.getBusinessKey())){
					signatureLog.setBusinessKey(businessKey.toString());
				} else {
					signatureLog.setBusinessKey(signatureLog.getBusinessKey() + "," + businessKey.toString());
				}
			}
			if(null != AuditUtil.getCurrentAudit() && null != AuditUtil.getCurrentAudit().getOperationAudit()){
				signatureLog.setOperateLogUuid(AuditUtil.getCurrentAudit().getOperationAudit().getUuid());
			}
		}
		
	}
	
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(String baseInfoIds){
		this.deleteBaseInfo(baseInfoIds,null);
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="删除",operType="3")
	public void deleteBaseInfo(String baseInfoIds,SignatureLog signatureLog){
		deleteCollection(baseInfoIds,signatureLog);
	}
	
	private void deleteCollection(String ids,SignatureLog signatureLog) {
		if(ids.isEmpty()){
			throw new BAPException("请选择一条记录进行操作");
		}
		String[] idst = ids.split(",");
		for(String idVersion : idst) {
			String id = idVersion.split("@")[0];
			String version = idVersion.split("@")[1];
			if (id != null && id.trim().length() > 0 && version != null && version.trim().length() > 0) {
				deleteBaseInfo(Long.valueOf(id), Integer.valueOf(version),signatureLog);
			}
		}
		if(null != signatureLog){
			if(null != AuditUtil.getCurrentAudit() && null != AuditUtil.getCurrentAudit().getOperationAudit()){
				signatureLog.setOperateLogUuid(AuditUtil.getCurrentAudit().getOperationAudit().getUuid());
			}
			String msgId="moduleCode:BEAM_1.0.0#entityCode:BEAM_1.0.0_baseInfo#modelCode:BEAM_1.0.0_baseInfo_BaseInfo#timeStamp:"+String.valueOf(new Date().getTime());
			reliableMessageSenderService.sendQueue(msgId,signatureLog);
		}
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="还原",operType="10")
	public void restoreBaseInfo(String baseInfoIds){
		restoreCollection(baseInfoIds);
	}
	
	public void restoreCollection(String ids) {
		String[] idst = ids.split(",");
		for(String idVersion : idst) {
			String id = idVersion.split("@")[0];
			if (id != null && id.trim().length() > 0) {
				restoreBaseInfo(Long.valueOf(id));
			}
		}
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="还原",operType="10")
	public void restoreBaseInfo(long baseInfoId){
		
		findBusinessKeyUsed(baseInfoId);	//判断业务主键是否重复
		
		BEAMBaseInfo baseInfo = getBaseInfo(baseInfoId);
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		beforeRestoreBaseInfo(baseInfo);
		baseInfo.setValid(true);
		baseInfoDao.update(baseInfo);
		afterRestoreBaseInfo(baseInfo);
		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		params.add(baseInfo);
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		props.put("eventType", "restore");
	}
	
	public void findBusinessKeyUsed(long baseInfoId){
		Property property = modelServiceFoundation.getBussinessProperty("BEAM_1.0.0_baseInfo_BaseInfo");
		if(property != null){
			String propertyName = property.getColumnName();
			String sql  = "select * from " + BEAMBaseInfo.TABLE_NAME + " where valid = 1 and " + 
					propertyName + " =  (select "+ propertyName +" from "+ BEAMBaseInfo.TABLE_NAME +" where id = ? )";
			List<Object> list =  baseInfoDao.createNativeQuery(sql,baseInfoId).list();
			if(list.size() > 0){
				throw new BAPException("被还原数据的业务主键和其它正常数据的业务主键重复，不允许还原");
			}
			
		}
	}
	
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="批量导入",operType="4")
	public  void batchImportBaseBaseInfo(List<BEAMBaseInfo>  baseInfos){
		for(BEAMBaseInfo baseInfo:baseInfos)  {
			saveBaseInfo(baseInfo, null, null, null);
		}
	}
	
	@Override
	public  void excelBatchImportBaseBaseInfo(List<BEAMBaseInfo>  baseInfos){
		for(BEAMBaseInfo baseInfo:baseInfos)  {
			saveBaseInfo(baseInfo, null);
		}
	}
	
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="保存")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads){
		saveBaseInfo(baseInfo, dgLists,dgDeleteIDs,assFileUploads, null);
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="保存")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads, String viewCode){
		saveBaseInfo(baseInfo, dgLists,dgDeleteIDs,assFileUploads, viewCode, null,null);
	}


	public void generateBaseInfoCodes(BEAMBaseInfo baseInfo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		generateBaseInfoCodes(baseInfo, false);
	}
	
	public void generateBaseInfoCodes(BEAMBaseInfo baseInfo, Boolean viewIsView) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
			baseInfoDao.flush();
			baseInfoDao.clear();
			BEAMBaseInfo modelinstance = getBaseInfo(baseInfo.getId());
			customGenerateCodes(modelinstance, viewIsView);
			ArrayList<Map<String, Object>> configs=null;
			String dependence;
			Property property=null;
			boolean isLegal;
		// code
		if (baseInfo.getCode() == null || baseInfo.getCode().isEmpty()) {
			String code_property_code = "BEAM_1.0.0_baseInfo_BaseInfo_code";
			property = modelServiceFoundation.getProperty(code_property_code);
			isLegal=true;
			if(null != property){
				Map<String, Object> codeattributesMap = property.getAttributesMap();
				if(codeattributesMap.get("pattern")!=null){
					codeCounter.setPattern(codeattributesMap.get("pattern").toString());
				}
				configs=(ArrayList<Map<String, Object>>) codeattributesMap.get("config");
				dependence=null;
				ArrayList<Object> codeparamlist=new ArrayList<Object>();
				for(int i=0;i<configs.size();i++){
					String ovalue=null;
					if(configs.get(i).get("type").equals("auto")&&configs.get(i).get("autoType").equals("Date")){
						Map<String, Object> autoConfig=configs.get(i);
						if(autoConfig.get("value").equals("_systemdate")){
							dependence=NonCachedCounterBean.dateFormat(new Date(),autoConfig.get("countType").toString());
						}else{
							String field=autoConfig.get("value").toString();
							if(field.equals("")){
								isLegal=false;
								break;
							}
							String fieldValue=ReflectUtils.getPropertyValue(modelinstance,field.substring(field.indexOf(".")+1));
							if(fieldValue==null||fieldValue.equals("")){
								isLegal=false;
								break;
							}else{
								if(autoConfig.get("countType").equals("Yearly")){
									dependence = fieldValue.substring(0, 4);
								}else if(autoConfig.get("countType").equals("Monthly")){
									dependence = fieldValue.substring(0, 7);
								}else if(autoConfig.get("countType").equals("Daily")){
									dependence = fieldValue.substring(0, 10);
								}
							}
						}
					}
					if(configs.get(i).get("type").equals("property")){
						ovalue=ReflectUtils.getPropertyValue(modelinstance,configs.get(i).get("value").toString().substring(configs.get(i).get("value").toString().indexOf(".")+1));
						if(ovalue.equals("")){
							isLegal=false;
							break;
						}
					}else if(configs.get(i).get("type").equals("date")){
						if(configs.get(i).get("value").equals("_systemdate")){
							ovalue=NonCachedCounterBean.dateFormat(new Date(),configs.get(i).get("dateType").toString());
						}else{
							String field=configs.get(i).get("value").toString();
							if(field.equals("")){
								isLegal=false;
								break;
							}
							String fieldValue=ReflectUtils.getPropertyValue(modelinstance,field.substring(field.indexOf(".")+1));
							if(fieldValue==null||fieldValue.equals("")){
								isLegal=false;
								break;
							}else{
								if(configs.get(i).get("dateType").equals("YearA")){
									ovalue=fieldValue.substring(0, 4);
								}else if(configs.get(i).get("dateType").equals("YearB")){
									ovalue=fieldValue.substring(2, 4);
								}else if(configs.get(i).get("dateType").equals("Month")){
									ovalue=fieldValue.substring(5, 7);
								}else if(configs.get(i).get("dateType").equals("Date")){
									ovalue=fieldValue.substring(8, 10);
								}
							}
						}
					}else{
						ovalue=configs.get(i).get("value").toString();
					}
					if(configs.get(i).get("thecase").equals("uppercase")){
						ovalue=ovalue.toUpperCase();
					}else if(configs.get(i).get("thecase").equals("lowercase")){
						ovalue=ovalue.toLowerCase();
					}
					codeparamlist.add(ovalue);
				}
				if(isLegal){
					String codenewCodeValue="";
					if(dependence!=null){
						if((Boolean)codeattributesMap.get("rollbackable")==true){
							codenewCodeValue=codeCounter.incrementAndGetStringDependenceWithTx(dependence,codeparamlist.toArray());
						}else{
							codenewCodeValue=codeCounter.incrementAndGetStringDependence(dependence,codeparamlist.toArray());
						}
					}else{
						if((Boolean)codeattributesMap.get("rollbackable")==true){
							codenewCodeValue=codeCounter.incrementAndGetStringWithTx(codeparamlist.toArray());
						}else{
							codenewCodeValue=codeCounter.incrementAndGetString(codeparamlist.toArray());
						}
					}
					modelinstance.setCode(codenewCodeValue);
				}
				
				if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
					if(property.getIsBussinessKey()){
						AuditUtil.setColumnStringB(modelinstance.getCode());
					}
					if(property.getIsMainDisplay()){
						AuditUtil.setColumnStringA(modelinstance.getCode());
					}
				}
			}
				
		}
		if (viewIsView) {
			baseInfoDao.saveWithRevertVersion(modelinstance);
		} else {
			baseInfoDao.save(modelinstance);
		}
	}
	
	public void generateBaseInfoSummarys(BEAMBaseInfo baseInfo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		generateBaseInfoSummarys(baseInfo, false);
	}

	public void generateBaseInfoSummarys(BEAMBaseInfo baseInfo, Boolean viewIsView) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void saveBaseInfo(BEAMBaseInfo baseInfo,Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads,String viewCode, String eventTopic,boolean... isImport){
		this.saveBaseInfo(baseInfo, dgLists,dgDeleteIDs,assFileUploads, viewCode, eventTopic,null,isImport);
	}

	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void saveBaseInfo(BEAMBaseInfo baseInfo,Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads,String viewCode, String eventTopic, SignatureLog signatureLog,boolean... isImport){
		boolean isNew = false;
		String entityCode = "BEAM_1.0.0_baseInfo";
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		String url = null;
		if(baseInfo.getId() != null && baseInfo.getId() > 0){
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("修改");
				AuditUtil.setAuditOperationType("2");
			}
			props.put("eventType", "modify");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/modify";
		}else{
			isNew = true;
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("新增");
				AuditUtil.setAuditOperationType("1");
			}
			props.put("eventType", "add");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/add";
		}
		List<Event> events = null;
		Boolean viewIsView = false;
		if(viewCode != null && !viewCode.trim().isEmpty()){
			View view = viewServiceFoundation.getView(viewCode);
			if(null != view) {
				viewIsView = (view.getType() == ViewType.VIEW);
			}
			events = viewServiceFoundation.getEventsByView(view);
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "beforeSave", events, baseInfo);
			}
		}
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo, viewIsView);

		if (viewIsView) {
			baseInfoDao.saveWithRevertVersion(baseInfo);
		} else {
			if(isNew)
				baseInfoDao.save(baseInfo);
			else
				baseInfoDao.merge(baseInfo);
		}
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		dealDatagridsSave(baseInfo,viewCode,dgLists,dgDeleteIDs,assFileUploads);
		// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		
		// 根据配置规则生成编码
		try {
			generateBaseInfoCodes(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		// 根据配置规则生成摘要
		try {
			generateBaseInfoSummarys(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		
		afterSaveBaseInfo(baseInfo, viewIsView);


		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
		if(viewCode != null){
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "afterSave", events, baseInfo);
			}
		}
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		baseInfoDao.flush();
		baseInfoDao.clear();
		baseInfo = baseInfoDao.load(baseInfo.getId());
		
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		
		params.add(baseInfo);
		
		if(signatureLog != null) {
			Object businessKey=null;
			businessKey=baseInfo.getCode();
			if(businessKey != null) {
				signatureLog.setBusinessKey(businessKey.toString());
			}
			if(null != AuditUtil.getCurrentAudit() && null != AuditUtil.getCurrentAudit().getOperationAudit()){
				signatureLog.setOperateLogUuid(AuditUtil.getCurrentAudit().getOperationAudit().getUuid());
			}
			signatureLog.setTableId(baseInfo.getId());
			String msgId="moduleCode:BEAM_1.0.0#entityCode:BEAM_1.0.0_baseInfo#modelCode:BEAM_1.0.0_baseInfo_BaseInfo#timeStamp:"+String.valueOf(new Date().getTime());
			reliableMessageSenderService.sendQueue(msgId,signatureLog);
		}
	}
	
	private void executeGScript(String entityCode, String scriptType, List<Event> events, BEAMBaseInfo baseInfo){
		for(Event e : events){
			if(e.getName().equals(scriptType)){
				if(e.getFunction()!=null && e.getFunction().length() > 0){
					try{
						Script script = scriptService.get(entityCode, e.getFunction());
						if (null == script || null == script.getCode()) {
							throw new BAPException("could not found the script.");
						}
						// 调用执行脚本方法
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("db", DbUtils.getInstance());
						variables.put("baseInfo", baseInfo);
						variables.put("userId", getCurrentUser().getId().toString());
						Object retObj = EngineScriptExecutor.eval(script.getCode(), variables);
					}catch(Exception ex){
						log.warn(ex.getMessage());
					}
				}
				break;
			}
		}
	}

	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void mergeBaseInfo(BEAMBaseInfo baseInfo, Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads){
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo);
		baseInfoDao.merge(baseInfo);
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		dealDatagridsSave(baseInfo,null,dgLists,dgDeleteIDs,assFileUploads);

			// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		afterSaveBaseInfo(baseInfo);
		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<BEAMBaseInfo> findBaseInfos(Page<BEAMBaseInfo> page, Criterion... criterions) {
		return baseInfoDao.findByPage(page, criterions);
	}
	@Override
	public List<Object> generateParamExcelSql(List<Param> params, Integer type, String viewCode){
		List<Object> returnList = new ArrayList<Object>();
		Boolean crossCompanyFlag = null;
		StringBuilder s = new StringBuilder();
		List<Object> list = new ArrayList<Object>();
		boolean hasWhere = false;
		String referenceCondition = "";
		String customerSql = "";
		String customerCondition = "";
		String classifySql = "";
		if (null != params && !params.isEmpty()) {
			Param param = params.get(0);
			if ("crossCompanyFlag".equals(param.getName())) {
				crossCompanyFlag = Boolean.parseBoolean(param.getValue()
						.toString());
				params.remove(0);
			}
		}

		if (null != params && !params.isEmpty()) {
			s.append(" AND ((");
			hasWhere = true;
			String advQuery = "";
			String classifyCodes = "";
			List<Object> advValues = null;
			String extraQuery = "";
			List<Object> customerValues = null;
			List<Object> extraQueryValues = null;
			List<Object> customerSqlValues = null;
			List<Object> classifySqlValues = null;
			// 组合where条件
			for (int i = 0; i < params.size(); i++) {
				Param param = params.get(i);

				if ("classifySql".equals(param.getName())) {
					classifySql = (String) param.getValue();
					continue;
				}

				if ("classifySqlValues".equals(param.getName())) {
					classifySqlValues = (List<Object>) param.getValue();
					continue;
				}

				if (type == Sql.TYPE_LIST_REFERENCE
						&& "referenceCondition".equals(param.getName())) {
					referenceCondition = (String) param.getValue();
					continue;
				}

				if ("customerValues".equals(param.getName())) {
					customerValues = (List<Object>) param.getValue();
					continue;
				}
				if ("customerSqlValues".equals(param.getName())) {
					customerSqlValues = (List<Object>) param.getValue();
					continue;
				}
				if ("customerCondition".equals(param.getName())) {
					customerCondition = (String) param.getValue();
					continue;
				}
				if ("customerSql".equals(param.getName())) {
					customerSql = (String) param.getValue();
					continue;
				}

				if ("advQueryCond".equals(param.getName())) {
					AdvQueryCondition cond = conditionService
							.toSql((String) param.getValue());
					if (cond != null) {
						advValues = cond.getValues();
						advQuery = cond.getSql();
					}
					continue;
				}
				// 数据分类内的高级查询
				if ("classifyCodes".equals(param.getName())) {
					classifyCodes = (String) param.getValue();
					continue;
				}
				if ("extraQueryCond".equals(param.getName())) {
					Map<String, String> oneToManyParams = new HashMap<String, String>();
					oneToManyParams = (Map<String, String>) param.getValue();
					String json = sqlService.getExtraQueryJson(viewCode);
					Pattern oneToManyPattern = Pattern
							.compile("(\"value\":\")(\\$\\$.*?\\$\\$)(\")");
					Matcher matcher = oneToManyPattern.matcher(json);
					while (matcher.find()) {
						String tag = (matcher.group(2));
						String subTag = tag.substring(2, tag.length() - 2);
						if (null != oneToManyParams.get(subTag)
								&& ((String) (oneToManyParams.get(subTag)))
										.length() > 0) {
							json = json.replace(
									tag,
									oneToManyParams.get(subTag).replace("\"",
											"SYMBOL_DOUBLE_QUOTE"));
						} else {
							json = json.replace(tag, "");
						}
					}
					if (null != json && json.length() > 0) {
						AdvQueryCondition cond = conditionService.toSql(json);
						if (cond != null) {
							extraQueryValues = cond.getValues();
							extraQuery = cond.getSql();
						}
					}
					continue;
				}
				if ("dataTable-sortColKey".equals(param.getName())) {
					String sortValue = (String) param.getValue();
					String key = null, columnName = null, customKey = null;
					if (sortValue.indexOf("::") > 0) {
						key = sortValue.split("::")[0];
						columnName = sortValue.split("::")[1];
					} else if (sortValue.indexOf(".") > 0) {
						customKey = sortValue.split("\\.")[0];
						if (customKey.contains("attrMap")) {
							key = customKey;
							columnName = sortValue.split("\\.")[1];
						} else {
							key = sortValue;
						}
					} else {
						key = sortValue;
					}
					int lastDotPos = key.lastIndexOf('.');
					if (null == columnName) {
						columnName = Inflector.getInstance().columnize(
								key.substring(lastDotPos + 1));
					}
					String tableAlias = lastDotPos < 0 ? "\"maintenanceList\""
							: "\"" + key.substring(0, lastDotPos) + "\"";
					if ("\"pending\"".equals(tableAlias)) {
						tableAlias = "\"p\"";
					}
					// sortOrderByStr.append(tableAlias).append(".").append(columnName);
					continue;
				}
				if ("dataTable-sortColOrder".equals(param.getName())) {
					// sortOrderByStr.append(" ").append((String)
					// param.getValue());
					continue;
				}
				if (param.getName().startsWith("\"tree-")) {
					if (param.getName().startsWith("\"tree-layRec-")) {
						String treeCondition = sqlService.getSqlQuery(viewCode,
								Sql.TYPE_USED_TREE);
						if (treeCondition != null
								&& treeCondition.trim().length() > 0) {
							if (hasWhere)
								s.append(" AND ");
							else
								s.append(" WHERE (");

							s.append(treeCondition);
							if (param.getLikeType() == Param.EQUAL_LIKELEFT) {
								list.add(param.getValue());
								list.add((String) param.getValue() + "-%");
							}
							if (param.getLikeType() == Param.LIKE_UNSUPPORT) {
								list.add(param.getValue());
							}
						}
					}
					continue;
				}
				if (!param.getName().startsWith("\"tree-")
						&& param.getLikeType() == Param.EQUAL_LIKELEFT) {
					s.append(" AND ( ").append(param.getName()).append("= ? ")
							.append(" OR ").append(param.getName())
							.append(" like ? )");
					list.add(param.getValue());
					list.add(param.getValue() + "-%");
					continue;
				}
				if (i > 0) {
					s.append(" AND ");
				}
				if ((null != param.getContainLower() && param.getContainLower())) {
					s.append(" ( ");
				}
				if (!param.getCaseSensitive()) {
					if (param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT
							|| param.getLikeType() == Param.LIKE_UNSUPPORT || param.getLikeType() == Param.NONE_EQUAL) {
						if (!"DATE".equals(param.getColumnType()) && !"DATETIME".equals(param.getColumnType()) && !"LONG".equals(param.getColumnType())
								&& !"INTEGER".equals(param.getColumnType()) && !"DECIMAL".equals(param.getColumnType()) && !"BOOLEAN".equals(param.getColumnType())) {
							s.append(" UPPER (");
						}
					}
				}
				if (param.getLikeType() != Param.MULTI_LIKE) {
					s.append(param.getName());
				}
				if (!param.getCaseSensitive()) {
					if (param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT
							|| param.getLikeType() == Param.LIKE_UNSUPPORT || param.getLikeType() == Param.NONE_EQUAL) {
						if (!"DATE".equals(param.getColumnType()) && !"DATETIME".equals(param.getColumnType()) && !"LONG".equals(param.getColumnType())
								&& !"INTEGER".equals(param.getColumnType()) && !"DECIMAL".equals(param.getColumnType()) && !"BOOLEAN".equals(param.getColumnType())) {
							s.append(") ");
						}
					}
				}
				String exp = " = ?";
				if (param.getLikeType() == Param.LIKE_UNSUPPORT) {
					/*if ((Boolean) param.getValue() == true) {
						s.append(" = 1");
					} else {
						s.append(" = 0");
					}*/
					s.append(" = ?");
				} else if (param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_RIGHT || param.getLikeType() == Param.LIKE_LEFT) {
					//s.append(" LIKE '%" + param.getValue() + "%'");
					s.append(" LIKE ?");
					exp = " LIKE ?";
				} else if (param.getLikeType() == Param.GREATE_EQUAL) {
					//s.append(" <= '" + param.getValue() + "'");
					s.append(" <= ?");
					exp = " >= ?";
				} else if (param.getLikeType() == Param.LESS_EQUAL) {
					//s.append(" <= '" + param.getValue() + "'");
					s.append(" <= ?");
					exp = " <= ?";
				} else if (param.getLikeType() == Param.GREATE_THAN) {
					//s.append(" > '" + param.getValue() + "'");
					s.append(" > ?");
					exp = " > ?";
				} else if (param.getLikeType() == Param.LESS_THAN) {
					//s.append(" < '" + param.getValue() + "'");
					s.append(" < ?");
					exp = " < ?";
				} else if (param.getLikeType() == Param.NONE_EQUAL) {
					//s.append(" <> '" + param.getValue() + "'");
					s.append(" <> ?");
					exp = " <> ?";
				} else if (param.getLikeType() == Param.MULTI_LIKE) {
					exp = " LIKE ?";
					String multiValue = param.getValue().toString();
					String[] values = multiValue.split(",");
					StringBuilder multiSb = new StringBuilder();
					for (int m = 0; m < values.length; m++) {
						if (null != values[m] && values[m].length() > 0) {
							multiSb.append(" OR ");
							multiSb.append(param.getName()).append(" LIKE ? ");
							list.add("%," + values[m] + ",%");
						}
					}
					if (multiSb.length() > 0) {
						s.append(" ( ");
						s.append(multiSb.toString().substring(4));
						s.append(" ) ");
					}
				} else {
				}
				if (param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT) {
					String upperStr = (String) param.getValue();
					if (!param.getCaseSensitive()) {
						upperStr = upperStr.toUpperCase();
					}
					if (param.getLikeType() == Param.LIKE_ALL) {
						param.setValue('%' + upperStr + '%');
					}
					if (param.getLikeType() == Param.LIKE_LEFT) {
						param.setValue(upperStr + '%');
					}
					if (param.getLikeType() == Param.LIKE_RIGHT) {
						param.setValue('%' + upperStr);
					}
				}
				if (param.getLikeType() == Param.LIKE_UNSUPPORT || param.getLikeType() == Param.NONE_EQUAL) {
					if ("DATETIME".equals(param.getColumnType()) || "DATE".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(DateUtils.ecDateFormat((String) param.getValue()));
						}
					} else if ("LONG".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(Long.parseLong((String) param.getValue()));
						}
					} else if ("INTEGER".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(Integer.parseInt((String) param.getValue()));
						}
					} else if ("DECIMAL".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(new BigDecimal((String) param.getValue()));
						}
					} else {
						if (param.getValue() instanceof String) {
							String upperStr = (String) param.getValue();
							if (!param.getCaseSensitive()) {
								upperStr = upperStr.toUpperCase();
							}
							param.setValue(upperStr);
						} else {
							param.setValue(param.getValue());
						}
					}
				}
				if (param.getLikeType() == Param.GREATE_EQUAL || param.getLikeType() == Param.GREATE_THAN || param.getLikeType() == Param.LESS_EQUAL || param.getLikeType() == Param.LESS_THAN) {
					if ("DATETIME".equals(param.getColumnType()) || "DATE".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(DateUtils.ecDateFormat((String) param.getValue()));
						}
					} else if ("LONG".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(Long.parseLong((String) param.getValue()));
						}
					} else if ("INTEGER".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(Integer.parseInt((String) param.getValue()));
						}
					} else if ("DECIMAL".equals(param.getColumnType())) {
						if (param.getValue() instanceof String) {
							param.setValue(new BigDecimal((String) param.getValue()));
						}
					}

				}

				if (param.getLikeType() != Param.MULTI_LIKE) {
					list.add(param.getValue());
				}
				if (null != param.getContainLower() && param.getContainLower()) {
					String prefix = param.getName().substring(0, param.getName().lastIndexOf("."));
					String columnName = param.getName().substring(param.getName().lastIndexOf(".") + 1);
					if (null != param.getModelInfo() && param.getModelInfo().length > 0) {
						String entityInfo = (param.getModelInfo())[0];
						String serviceInfo = (param.getModelInfo())[1];
						String layRecColumnName = null;
						if (param.getModelInfo().length > 2) {
							layRecColumnName = (param.getModelInfo())[2];
						}
						ServiceReference ref = bundleContext.getServiceReference(serviceInfo);
						List<String> layRecs = null;
						if (null != ref) {
							IModelTreeLayRecService layRecService = (IModelTreeLayRecService) bundleContext.getService(ref);
							layRecs = layRecService.getContainLower(entityInfo, Inflector.getInstance().columnToField(columnName), exp, param.getValue(), false);
						}
						if (null != layRecs && !layRecs.isEmpty()) {
							String layRecCond = "";
							for (String layRec : layRecs) {
								layRecCond += " OR " + prefix + "." + (layRecColumnName == null ? "LAY_REC" : layRecColumnName) + " = ?";
								layRecCond += " OR " + prefix + "." + (layRecColumnName == null ? "LAY_REC" : layRecColumnName) + " like ?";
								list.add(layRec);
								list.add(layRec + "-%");
							}
							s.append(layRecCond);
						}
						s.append(" ) ");
					}
				}
			}
			if (advQuery != null && advQuery.length() > 0) {
				s.append(" AND ").append(advQuery);
				if (advValues != null) {
					list.addAll(advValues);
				}
			}
			if (extraQuery != null && extraQuery.length() > 0) {
				s.append(" AND ").append(extraQuery);
				if (extraQueryValues != null) {
					list.addAll(extraQueryValues);
				}
			}
			if (customerCondition != null && customerCondition.length() > 0) {
				s.append(" AND ").append(customerCondition);
				if (customerValues != null && customerValues.size() > 0) {
					list.addAll(customerValues);
				}
			}
			if (customerSql != null && customerSql.length() > 0) {
				s.append(" AND ").append(customerSql);
				if (customerSqlValues != null && customerSqlValues.size() > 0) {
					list.addAll(customerSqlValues);
				}
			}
			if (classifySql != null && classifySql.length() > 0) {
				s.append(" AND (").append(classifySql).append(")");
				if (classifySqlValues != null && classifySqlValues.size() > 0) {
					list.addAll(classifySqlValues);
				}
			}
			s.append(" ) ");
		}
		s.append(" ) ");

		returnList.add(s.toString());
		returnList.add(list);
		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	private BEAMBaseInfoDealInfo getDealInfoEntity(Long tableInfoId) {
		
		List<BEAMBaseInfoDealInfo> list=baseInfoDao.createQuery("from " + BEAMBaseInfoDealInfo.JPA_NAME + " where tableInfoId=?", tableInfoId).setMaxResults(1).list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public void saveDealInfo(BEAMBaseInfoDealInfo dealInfo) {
		baseInfoDao.saveDealInfo(dealInfo);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public int getDealInfoCount(Long tableInfoId) {
		String sql = "SELECT count(1) totalCount " + "FROM "+BEAMBaseInfoDealInfo.TABLE_NAME+" DI " + "LEFT JOIN " + User.TABLE_NAME + " U ON DI.USER_ID=U.ID " + "LEFT JOIN " + Staff.TABLE_NAME
				+ " S ON S.ID=U.STAFF_ID " + "WHERE DI.TABLE_INFO_ID = ?";
		Number list = (Number) baseInfoDao.createNativeQuery(sql, tableInfoId).uniqueResult();
		return list.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Object[]> findDealInfos(Long tableInfoId, boolean expandFlag) {
		String sql = "SELECT DI.TASK_DESCRIPTION ACTIVITYNAME,DI.CREATE_TIME CREATETIME ,DI.COMMENTS COMMENTS,DI.OUTCOME OUTCOME,S.NAME STAFFNAME,DI.OUTCOME_DES OUTCOME_DES,DI.ASSIGN_STAFF ASSIGNSTAFF ,DEALINFO_TYPE DEALINFOTYPE, DI.PROXY_STAFF PROXYSTAFF, DI.PENDING_CREATE_TIME PCREATETIME,  DI.SIGNATURE SIGNATURE, WT.TYPE "
				+ " FROM "+BEAMBaseInfoDealInfo.TABLE_NAME + " DI " 
				+ " LEFT JOIN WF_TASK WT ON WT.CODE = DI.ACTIVITY_NAME AND WT.PROCESS_KEY = DI.PROCESS_KEY AND WT.PROCESS_VERSION = DI.PROCESS_VERSION "
				+ " LEFT JOIN " + User.TABLE_NAME + " U ON DI.USER_ID=U.ID " + "LEFT JOIN " + Staff.TABLE_NAME + " S ON S.ID=U.STAFF_ID "
				+ " WHERE DI.TABLE_INFO_ID = ? AND DI.TASK_DESCRIPTION is not null ";
		if (expandFlag) {
			sql += " AND DI.COMMENTS IS NOT NULL";
			DBTYPE dbtype = baseInfoDao.getDBType();
			if (DBTYPE.MSSQL == dbtype) {
				sql += " AND DI.COMMENTS != ''";
			}
		}
		sql += " ORDER BY DI.SORT ASC, DI.CREATE_TIME ASC, DI.ID ASC";
		List<Object[]> list = baseInfoDao.createNativeQuery(sql, tableInfoId).list();
		for(Object[] obj:list){
			if(obj[2]!=null){
				obj[2]=HtmlUtils.htmlEscape(obj[2].toString().trim());
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Map<String, List<Object[]>> findDealInfosGroup(Long tableInfoId, boolean expandFlag) {
		StringBuilder groupSql = new StringBuilder();
		groupSql.append("SELECT DI.TASK_DESCRIPTION ACTIVITYNAME,DI.CREATE_TIME CREATETIME ,DI.COMMENTS COMMENTS,DI.OUTCOME OUTCOME,S.NAME STAFFNAME,DI.OUTCOME_DES OUTCOME_DES,DI.ASSIGN_STAFF ASSIGNSTAFF ,DEALINFO_TYPE DEALINFOTYPE, DI.PROXY_STAFF PROXYSTAFF, DI.PENDING_CREATE_TIME PCREATETIME,  DI.SIGNATURE SIGNATURE, DI.ACTIVITY_NAME AN, WT.NAME TDESC, WT.TYPE, WT.SHOW_IN_SIMPLE_DEALINFO ");
		groupSql.append(" FROM "
				+ BEAMBaseInfoDealInfo.TABLE_NAME
				+ " DI LEFT JOIN WF_TASK WT ON WT.CODE = DI.ACTIVITY_NAME AND WT.PROCESS_KEY = DI.PROCESS_KEY AND WT.PROCESS_VERSION = DI.PROCESS_VERSION ");
		groupSql.append(" LEFT JOIN " + User.TABLE_NAME + " U ON DI.USER_ID=U.ID LEFT JOIN " + Staff.TABLE_NAME + " S ON S.ID=U.STAFF_ID ");
		groupSql.append(" WHERE DI.TABLE_INFO_ID = ? AND DI.TASK_DESCRIPTION IS NOT NULL ");
		if (expandFlag) {
			groupSql.append(" AND DI.COMMENTS IS NOT NULL");
			DBTYPE dbtype = baseInfoDao.getDBType();
			if (DBTYPE.MSSQL == dbtype) {
				groupSql.append(" AND DI.COMMENTS != ''");
			}
		}
		groupSql.append(" ORDER BY WT.ROUTE_SEQUENCE ASC, DI.SORT ASC, DI.CREATE_TIME ASC, DI.ID ASC");

		List<Object[]> groupList = baseInfoDao.createNativeQuery(groupSql.toString(), tableInfoId).list();
		Map<String, List<Object[]>> groupMap = new LinkedHashMap<String, List<Object[]>>();
		List<Object[]> otherList = new ArrayList<Object[]>();
		for (Object[] obj : groupList) {
			if (obj[0] != null) {
				obj[0] = InternationalResource.get(obj[0].toString(), getCurrentLanguage());
			}
			if (obj[2] != null) {
				obj[2] = HtmlUtils.htmlEscape(obj[2].toString().trim());
			}
			if (obj[12] != null) {
				obj[12] = InternationalResource.get(obj[12].toString(), getCurrentLanguage());
			} else {
				if(null != obj[11]) {
					obj[12] = InternationalResource.get(obj[11].toString(), getCurrentLanguage());
				}
			}
			if(null != obj[11]) {
				if (!groupMap.containsKey(obj[11].toString())) {
					List<Object[]> diList = new ArrayList<Object[]>();
					diList.add(obj);
					groupMap.put(obj[11].toString(), diList);
				} else {
					List<Object[]> exsitDiList = groupMap.get(obj[11].toString());
					exsitDiList.add(obj);
					groupMap.put(obj[11].toString(), exsitDiList);
				}
			} else {
				otherList.add(obj);
			}
		}
		if(null != otherList && !otherList.isEmpty()) {
			groupMap.put("bap_other", otherList);
		}
		return groupMap;
	}

	private static String matchSql(Pattern p, String sql, String prefix, Map<String, String> maps, int n1, int n2) {
		Matcher matcher = p.matcher(sql);
		int i = 1;
		if (null == maps)
			maps = new HashMap<String, String>();
		while (matcher.find()) {
			String tag = (matcher.group(n1));// 含引号
			String tag2 = (matcher.group(n2));// 不含引号
			String symbol = prefix + i++;
			sql = sql.replace(tag, symbol);
			maps.put(symbol, tag2);
		}
		return sql;
	}
	
	private static String replaceSql(Pattern p, String sql, Map<String, String> maps, int n1, int n2) {
		Matcher matcher = p.matcher(sql);
		while (matcher.find()) {
			String tag = matcher.group(n1);
			String tag2 = matcher.group(n2);
			for(Map.Entry<String, String> entry : maps.entrySet()) {
				if(entry.getValue().equals(tag)) {
					sql = sql.replace(tag2,entry.getKey());
				}
			}
		}
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findBaseInfos(Page<BEAMBaseInfo> page,  String viewCode, int type, String processKey,Boolean flowBulkFlag, Boolean hasAttachment,Boolean noQueryFlag,String exportSql, Map exportMap, List<Param> params) {
		findBaseInfos(page,  viewCode, type, processKey, flowBulkFlag, hasAttachment, params, viewCode, noQueryFlag, exportSql, exportMap);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findBaseInfos(Page<BEAMBaseInfo> page,  String viewCode, int type, String processKey,Boolean flowBulkFlag, Boolean hasAttachment, List<Param> params, String permissionCode,Boolean noQueryFlag,String exportSql, Map exportMap) {
		commonQuery(page, viewCode, type, processKey, flowBulkFlag, hasAttachment, params, permissionCode, noQueryFlag, exportSql, exportMap);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findBaseInfos(Page<BEAMBaseInfo> page,  String viewCode, int type, String processKey,Boolean flowBulkFlag, Boolean hasAttachment, List<Param> params, String permissionCode, Boolean noQueryFlag,String exportSql, Map exportMap, Object... objects) {
		commonQuery(page, viewCode, type, processKey, flowBulkFlag, hasAttachment, params, permissionCode, noQueryFlag, exportSql, exportMap, objects);
	}
	
	/**
	 * 生成自定义字段查询sql
	 * @param viewCode 视图code
	 * @param sql 查询sql
	 * @return
	 */
	public String generateCustomPropertySql(String viewCode, String sql) {
		View v = viewServiceFoundation.getView(viewCode);
		if (v != null && v.getHasCustomSection() != null && v.getHasCustomSection()) {
			StringBuilder selectSql = new StringBuilder();
			StringBuilder joinSql = new StringBuilder();
			List<CustomPropertyViewMapping> viewMapppingList = viewServiceFoundation.getCustomPropertyViewMappings(viewCode);
			if (viewMapppingList != null && viewMapppingList.size() > 0) {
				Set<String> propLayRecSet = new HashSet<String>();
				for (CustomPropertyViewMapping viewMapping : viewMapppingList) {
					if (viewMapping.getPropertyLayRec() != null && viewMapping.getPropertyLayRec().length() > 0) {
						String[] layRecArr = viewMapping.getPropertyLayRec().split("\\|\\|");
						if (!layRecArr[0].contains(".")) {
						    if (null != sql && sql.indexOf(viewMapping.getProperty().getColumnName()) == -1) {
								selectSql.append(",\"").append(layRecArr[0]).append("\".").append(viewMapping.getProperty().getColumnName()).append(" AS ")
										.append("\"").append(viewMapping.getProperty().getName()).append("\"");
							}
						} else {
							propLayRecSet.add(viewMapping.getPropertyLayRec());
							String modelAlias = layRecArr[0].substring(layRecArr[0].indexOf(".") + 1);
							if (null != sql && sql.indexOf(viewMapping.getProperty().getColumnName()) == -1) {
								selectSql.append(",\"cp_").append(modelAlias).append("\".").append(viewMapping.getProperty().getColumnName()).append(" AS ")
										.append("\"cp_").append(modelAlias).append(".").append(viewMapping.getProperty().getName()).append("\"");
							}
						}
					}
				}
				if (propLayRecSet != null && propLayRecSet.size() > 0) {
					Set<String> tmpPropLayRecSet = new HashSet<String>();
					tmpPropLayRecSet.addAll(propLayRecSet);
					for (Iterator<String> iter = propLayRecSet.iterator(); iter.hasNext();) {
						String propLayRec = iter.next();
						String alias1 = propLayRec.split("\\|\\|")[0];
						for (String tmpPropLayRec : tmpPropLayRecSet) {
							String alias2 = tmpPropLayRec.split("\\|\\|")[0];
							if (!alias2.equals(alias1) && alias2.startsWith(alias1)) {
								iter.remove();
								break;
							}
						}
					}
					for (String propLayrec : propLayRecSet) {
						String[] layRecArr = propLayrec.split("\\|\\|");
						String modelAlias = layRecArr[0].substring(layRecArr[0].indexOf(".") + 1);
						String mainModelAlias = layRecArr[0].substring(0, layRecArr[0].indexOf("."));
						String[] modelAliasTuple = modelAlias.split("\\.");
						String[] relations = layRecArr[1].split("\\-");
						String modelAlias1 = "";
						String modelAlias2 = "";
						for (int i = 0; i < relations.length; i++) {
							String[] relationArr = relations[i].split(",");
							String tableName1 = relationArr[0];
							String colName1 = relationArr[1];
							// String tableName2 = relationArr[2];
							String colName2 = relationArr[3];
							modelAlias1 += (i > 0 ? "." + modelAliasTuple[i] : modelAliasTuple[i]);
							if (i > 0) {
								modelAlias2 += (i > 1 ? "." + modelAliasTuple[i - 1] : modelAliasTuple[i - 1]);
							}
							joinSql.append(" LEFT OUTER JOIN ").append(tableName1).append(" \"cp_").append(modelAlias1).append("\"").append(" ON \"cp_")
									.append(modelAlias1).append("\".").append(colName1).append(" = \"");
							if (i > 0) {
								joinSql.append("cp_").append(modelAlias2);
							} else {
								joinSql.append(mainModelAlias);
							}
							joinSql.append("\".").append(colName2);
						}
					}
				}
				if (selectSql.length() > 0) {
					sql = sql.substring(0, sql.indexOf(" FROM ")) + selectSql.toString() + sql.substring(sql.indexOf(" FROM "));
					if (joinSql.length() > 0) {
						sql += joinSql.toString();
					}
				}
			}
		}
		return sql;
	}
	
	/**
	 * @param page
	 * @param viewCode
	 * @param type
	 * @param processKey
	 * @param flowBulkFlag
	 * @param hasAttachment
	 * @param params
	 * @param permissionCode
	 */
	@SuppressWarnings("unchecked")
	public void commonQuery(Page<BEAMBaseInfo> page, String viewCode, int type, String processKey, Boolean flowBulkFlag,
			Boolean hasAttachment, List<Param> params, String permissionCode,Boolean noQueryFlag,String exportSql, Map exportMap, Object... objects) {
		int queryType = 0;
		if(objects.length > 0) {
			queryType = (int) objects[0];
		}
		String sql = "";
		String dgCode = "";
	
		if(viewCode.split(",").length < 2){
			sql = sqlService.getSqlQuery(viewCode, type);
		}else{
			dgCode = viewCode.split(",")[1];
			viewCode = viewCode.split(",")[0];
			sql = sqlService.getDGSqlQuery(dgCode,type);
		}
		//如果存在导出下配置的sql则进行替换
		
		if(!exportSql.trim().isEmpty() ) {
			if(page.isExportFlag() && sql != null && sql.contains("LEFT OUTER JOIN")){//如果用于导出，sql里的left outer join的内容拼到exportSql里
				String sql2 = sql;
				if(sql2.contains("LEFT OUTER JOIN")){
					sql2 = sql2.substring(sql2.indexOf("LEFT OUTER JOIN"), sql2.length());
					String tableNames[] = sql2.split("LEFT OUTER JOIN");
					for(String str : tableNames){
						if(str!=null && !str.equals("") && str.contains(" ON ")){
							String tableNames2[] = str.split(" ON ");
							if(tableNames2[0]!=null && !tableNames2[0].equals("") && !exportSql.contains(tableNames2[0].trim())){
								exportSql += " LEFT OUTER JOIN" + str;
							}
						}
					}
					sql = exportSql;
				}	
			}else{//如果存在导出下配置的sql则进行替换
				sql=exportSql;	
			}
		} 
		 
		// 自定义字段sql拼接
		sql = generateCustomPropertySql(viewCode, sql);
		String countSql = null;
		if(null != dgCode && !"".equals(dgCode)){
			countSql = sqlService.getDGSqlQuery(dgCode,Sql.TYPE_USED_TOTALS);
		}else{
			countSql = sqlService.getSqlQuery(viewCode,Sql.TYPE_USED_TOTALS);
		}
		if(!exportSql.trim().isEmpty())  {
			//FIXME  有小节配置时,需要将小节信息加入
			countSql="SELECT COUNT(*) count FROM ";	
		}
		User currentUser=(User)getCurrentUser();
		if(sql == null){
			sql = "";
		}
		StringBuilder totalSql = new StringBuilder(sql);
		Boolean crossCompanyFlag = null;
		if (null != sql && sql.length() > 0) {
			StringBuilder s = new StringBuilder();
			StringBuilder queryCond = new StringBuilder();
			List<Object> list = new ArrayList<Object>();
			Map<String,Object> customerSqlListMap = new HashMap<String,Object>();
			boolean hasWhere = false;
			String referenceCondition = "";
			String customerSql = "";
			String customerCondition = "";
			String classifySql = "";
			if (null != params && !params.isEmpty()) {
				Param param = params.get(0);
				if("crossCompanyFlag".equals(param.getName())){
					crossCompanyFlag = Boolean.parseBoolean(param.getValue().toString());
					params.remove(0);
				}
			}
			StringBuilder sortOrderByStr = new StringBuilder("");
			if (null != params && !params.isEmpty()) {
				s.append(" WHERE ((");
				hasWhere = true;
				String advQuery = "";
				String classifyCodes = "";
				List<Object> advValues = null;
				String fastQuery = "";
				List<Object> fastValues = null;
				String extraQuery = "";
				List<Object> customerValues = null;
				List<Object> extraQueryValues = null;
				List<Object> customerSqlValues = null;
				List<Object> classifySqlValues = null;
				//组合where条件
				for (int i = 0; i < params.size(); i++) {
					Param param = params.get(i);
					
					if("classifySql".equals(param.getName())){
						classifySql = (String) param.getValue();
						continue;
					}
					
					if("classifySqlValues".equals(param.getName())){
						classifySqlValues = (List<Object>) param.getValue();
						continue;
					}
					
					if(type == Sql.TYPE_LIST_REFERENCE && "referenceCondition".equals(param.getName())) {
						referenceCondition = (String) param.getValue();
						continue;
					}
					
					if("customerValues".equals(param.getName())){
						customerValues = (List<Object>) param.getValue();
						continue;
					}
					if("customerSqlValues".equals(param.getName())){
						customerSqlValues = (List<Object>) param.getValue();
						continue;
					}
					if("customerSqlListMap".equals(param.getName())){
						customerSqlListMap = (Map<String,Object>) param.getValue();
						continue;
					}
					if("customerCondition".equals(param.getName())){
						customerCondition = (String) param.getValue();
						continue;
					}
					if("customerSql".equals(param.getName())){
						customerSql = (String)param.getValue();
						continue;
					}
					
					if ("advQueryCond".equals(param.getName())) {
						AdvQueryCondition cond = conditionService.toSql((String) param.getValue());
						if (cond != null) {
							advValues = cond.getValues();
							advQuery = cond.getSql();
						}
						continue;
					}
					//数据分类内的高级查询
					if ("classifyCodes".equals(param.getName())) {
						classifyCodes = (String)param.getValue();
						continue;
					}
					
					if ("fastQueryCond".equals(param.getName())) {
						AdvQueryCondition cond = conditionService.toSql((String) param.getValue());
						if (cond != null) {
							fastValues = cond.getValues();
							fastQuery = cond.getSql();
						}
						continue;
					}
					
					if ("extraQueryCond".equals(param.getName())) {
						Map<String,String> oneToManyParams = new HashMap<String,String>();
						oneToManyParams = (Map<String, String>) param.getValue();
						String json = sqlService.getExtraQueryJson(viewCode);
						Pattern oneToManyPattern = Pattern.compile("(\"value\":\")(\\$\\$.*?\\$\\$)(\")");
						Matcher matcher = oneToManyPattern.matcher(json);
						while (matcher.find()) {
							String tag = (matcher.group(2));
							String subTag = tag.substring(2, tag.length()-2);
							if(null != oneToManyParams.get(subTag) && ((String)(oneToManyParams.get(subTag))).length() > 0) {
								json = json.replace(tag, oneToManyParams.get(subTag).replace("\"", "SYMBOL_DOUBLE_QUOTE"));
							} else {
								json = json.replace(tag, "");
							}
						}
						if(null != json && json.length() > 0) {
							AdvQueryCondition cond = conditionService.toSql(json);
							if (cond != null) {
								extraQueryValues = cond.getValues();
								extraQuery = cond.getSql();
							}
						}
						continue;
					}
					if ("dataTable-sortColKey".equals(param.getName())) {
						String sortValue = (String) param.getValue();
						String key = null, columnName = null,customKey = null;
						if(sortValue.indexOf("::") > 0) {
							key = sortValue.split("::")[0];
							columnName = sortValue.split("::")[1];
							if(key.indexOf(".") > 0 && key.contains("attrMap")){//自定义字段
								key = key.split("\\.")[1];
								if(key.startsWith("cp_")){//判断排序字段是否是关联模型的字段
									key = key.substring(3).replace("_", ".");
								}
							}
						} else if (sortValue.indexOf(".") > 0){
							customKey = sortValue.split("\\.")[0];
							if(customKey.contains("attrMap")){
								key =customKey;
								columnName = sortValue.split("\\.")[1];
							}else{
								key = sortValue;
							}
						} else {
							key = sortValue;
						}
						int lastDotPos = key.lastIndexOf('.');
						if(null == columnName) {
							columnName = Inflector.getInstance().columnize(key.substring(lastDotPos + 1));
						}
						String tableAlias = lastDotPos < 0 ? "\"baseInfo\"" : "\"" + key.substring(0, lastDotPos) + "\"";
						if("\"pending\"".equals(tableAlias)) {
							tableAlias = "\"p\"";
						}
						sortOrderByStr.append(tableAlias).append(".").append(columnName);
						continue;
					}
					if ("dataTable-sortColOrder".equals(param.getName())) {
						sortOrderByStr.append(" ").append((String) param.getValue());
						continue;
					}
					if(param.getName()!=null&&param.getName().startsWith("\"tree-")){
						if(param.getName().startsWith("\"tree-layRec-")){
							String treeCondition = sqlService.getSqlQuery(viewCode,Sql.TYPE_USED_TREE);
							if (treeCondition!=null && treeCondition.trim().length() > 0) {
								if (hasWhere){
									s.append(" AND ");
								}else{
									s.append(" WHERE (");
									hasWhere = true;
								}
									
								s.append(treeCondition);
								if(param.getLikeType() == Param.EQUAL_LIKELEFT) {
									list.add(param.getValue());
									list.add((String) param.getValue()+"-%");
								} 
								if(param.getLikeType() == Param.LIKE_UNSUPPORT) { 
									list.add(param.getValue());
								}
							}
						}
						continue;
					}
					if(param.getName()!=null&&!param.getName().startsWith("\"tree-") && param.getLikeType() == Param.EQUAL_LIKELEFT){
						s.append(" AND ( ").append(param.getName()).append("= ? ").append(" OR ")
							.append(param.getName()).append(" like ? )");
						list.add(param.getValue());
						list.add(param.getValue()+"-%");
						continue;
					}
					if (i > 0){
						s.append(" AND ");
					}
					if((null != param.getContainLower() && param.getContainLower())) {
						s.append(" ( ");
					}
					if(!param.getCaseSensitive()) {
						if(param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT || param.getLikeType() == Param.LIKE_UNSUPPORT  || param.getLikeType() == Param.NONE_EQUAL) {
							if(!"DATE".equals(param.getColumnType()) && !"DATETIME".equals(param.getColumnType()) && !"LONG".equals(param.getColumnType()) && !"INTEGER".equals(param.getColumnType()) && !"DECIMAL".equals(param.getColumnType()) && !"BOOLEAN".equals(param.getColumnType())) {
								s.append(" UPPER (");
							}
						}
					}
					if(param.getLikeType() != Param.MULTI_LIKE) {
						s.append(param.getName());
					}
					if(!param.getCaseSensitive()) {
						if(param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT || param.getLikeType() == Param.LIKE_UNSUPPORT  || param.getLikeType() == Param.NONE_EQUAL) {
							if(!"DATE".equals(param.getColumnType()) && !"DATETIME".equals(param.getColumnType()) && !"LONG".equals(param.getColumnType()) && !"INTEGER".equals(param.getColumnType()) && !"DECIMAL".equals(param.getColumnType()) && !"BOOLEAN".equals(param.getColumnType())) {
								s.append(") ");
							}
						}
					}
					String exp = " = ?";
					if (param.getLikeType() == Param.LIKE_UNSUPPORT) {
						s.append(" = ?");
					} else if(param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_RIGHT || param.getLikeType() == Param.LIKE_LEFT) {
						s.append(" LIKE ?");
						exp = " LIKE ?";
					} else if(param.getLikeType() == Param.GREATE_EQUAL) {
						s.append(" >= ?");
						exp = " >= ?";
					}else if(param.getLikeType() == Param.LESS_EQUAL) {
						s.append(" <= ?");
						exp = " <= ?";
					}else if (param.getLikeType() == Param.GREATE_THAN) {
						s.append(" > ?");
						exp = " > ?";
					}else if (param.getLikeType() == Param.LESS_THAN) {
						s.append(" < ?");
						exp = " < ?";
					}else if (param.getLikeType() == Param.NONE_EQUAL) {
						s.append(" <> ?");
						exp = " <> ?";
					}else if (param.getLikeType() == Param.MULTI_LIKE) {
						exp = " LIKE ?";
						String multiValue = param.getValue().toString();
						String[] values = multiValue.split(",");
						StringBuilder multiSb = new StringBuilder();
						for(int m = 0; m < values.length; m++) {
							if(null != values[m] && values[m].length() > 0) {
								multiSb.append(" OR ");
								multiSb.append(param.getName()).append(" LIKE ? ");
								list.add("%," + values[m] + ",%");
							}
						}
						if(multiSb.length() > 0) {
							s.append(" ( ");
							s.append(multiSb.toString().substring(4));
							s.append(" ) ");
						}
					}else {}
					if(param.getLikeType() == Param.LIKE_ALL || param.getLikeType() == Param.LIKE_LEFT || param.getLikeType() == Param.LIKE_RIGHT) {
						String upperStr = (String) param.getValue();
						if(!param.getCaseSensitive()) {
							upperStr = upperStr.toUpperCase();
						}
						if(param.getLikeType() == Param.LIKE_ALL) {
							param.setValue('%' + upperStr + '%');
						}
						if(param.getLikeType() == Param.LIKE_LEFT) {
							param.setValue(upperStr + '%');
						}
						if(param.getLikeType() == Param.LIKE_RIGHT) {
							param.setValue('%' + upperStr);
						}	
					}
					if(param.getLikeType() == Param.LIKE_UNSUPPORT  || param.getLikeType() == Param.NONE_EQUAL) {
						if ("DATETIME".equals(param.getColumnType()) || "DATE".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(DateUtils.ecDateFormat((String) param.getValue()));
							}
						} else if ("LONG".equals(param.getColumnType())){
							if(param.getValue() instanceof String){
								param.setValue(Long.parseLong((String) param.getValue()));
							}
						} else if ("INTEGER".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(Integer.parseInt((String) param.getValue()));
							}
						} else if ("DECIMAL".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(new BigDecimal((String) param.getValue()));
							}
						} else {
							if(param.getValue() instanceof String){
								String upperStr = (String) param.getValue();
								if(!param.getCaseSensitive()) {
									upperStr = upperStr.toUpperCase();
								} 
								param.setValue(upperStr);
							} else {
								param.setValue(param.getValue());
							}
						}
					}
					if(param.getLikeType() == Param.GREATE_EQUAL || param.getLikeType() == Param.GREATE_THAN || param.getLikeType() == Param.LESS_EQUAL || param.getLikeType() == Param.LESS_THAN) {
						if ("DATETIME".equals(param.getColumnType()) || "DATE".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(DateUtils.ecDateFormat((String) param.getValue()));
							}
						} else if ("LONG".equals(param.getColumnType())){
							if(param.getValue() instanceof String){
								param.setValue(Long.parseLong((String) param.getValue()));
							}
						} else if ("INTEGER".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(Integer.parseInt((String) param.getValue()));
							}
						} else if ("DECIMAL".equals(param.getColumnType())) {
							if(param.getValue() instanceof String){
								param.setValue(new BigDecimal((String) param.getValue()));
							}
						}					

					}
					
					if(param.getLikeType() != Param.MULTI_LIKE) {
						list.add(param.getValue());
					}
					if(null != param.getContainLower() && param.getContainLower()) {
						String prefix = param.getName().substring(0, param.getName().lastIndexOf("."));
						String columnName = param.getName().substring(param.getName().lastIndexOf(".") + 1);
						if(null != param.getModelInfo() && param.getModelInfo().length > 0) {
							String entityInfo = (param.getModelInfo())[0];
							String serviceInfo = (param.getModelInfo())[1];
							String layRecColumnName = null;
							if(param.getModelInfo().length > 2) {
								layRecColumnName = (param.getModelInfo())[2];
							}
							ServiceReference ref = bundleContext.getServiceReference(serviceInfo);
							List<String> layRecs = null;
							if(null != ref) {
								IModelTreeLayRecService layRecService = (IModelTreeLayRecService) bundleContext.getService(ref);
								layRecs = layRecService.getContainLower(entityInfo, Inflector.getInstance().columnToField(columnName), exp, param.getValue(), false);
							}
							if(null != layRecs && !layRecs.isEmpty()) {
								String layRecCond = "";
								for (String layRec : layRecs) {
									layRecCond += " OR " + prefix + "." + (layRecColumnName == null ? "LAY_REC" : layRecColumnName) +  " = ?";
									layRecCond += " OR " + prefix + "." + (layRecColumnName == null ? "LAY_REC" : layRecColumnName) +  " like ?";
									list.add(layRec);
									list.add(layRec + "-%");
								}
								s.append(layRecCond);
							}
							s.append(" ) ");
						}
					}
				}
					if (advQuery != null && advQuery.length() > 0) {
						s.append(" AND ").append(advQuery);
						if (advValues != null) {
							list.addAll(advValues);
						}
					}
					if (fastQuery != null && fastQuery.length() > 0) {
						s.append(" AND ").append(fastQuery);
						if (fastValues != null) {
							list.addAll(fastValues);
						}
					}
					if (extraQuery != null && extraQuery.length() > 0) {
						s.append(" AND ").append(extraQuery);
						if (extraQueryValues != null) {
							list.addAll(extraQueryValues);
						}
					}
					if(customerCondition !=null && customerCondition.length()>0){
						s.append(" AND ").append(customerCondition);
						if(customerValues!=null && customerValues.size()>0){
							list.addAll(customerValues);
						}
					}
					if(customerSql != null && customerSql.length()>0){
						s.append(" AND ").append(customerSql);
						if(customerSqlValues!=null && customerSqlValues.size()>0){
							list.addAll(customerSqlValues);
						}
					}
					if(classifySql != null && classifySql.length() > 0){
						s.append(" AND (").append(classifySql).append(")");
						if(classifySqlValues != null && classifySqlValues.size() > 0){
							list.addAll(classifySqlValues);
						}
					}
					
					if(type == Sql.TYPE_LIST_REFERENCE){
						if(null != crossCompanyFlag && !crossCompanyFlag && !getCurrentCompanyId().equals(1L)){
							s.append(" AND \"baseInfo\".CID in (1,").append(getCurrentCompanyId()+")");
						}
					}
					
	//				if(type == Sql.TYPE_LIST_QUERY || type == Sql.TYPE_LIST_PENDING || type == Sql.TYPE_LIST_REFERENCE) {
	//					if(type == Sql.TYPE_LIST_PENDING) {
	//						s.append(" AND \"p\".CID = ").append(getCurrentCompanyId());
	//					} else if(type == Sql.TYPE_LIST_QUERY){
	//						s.append(" AND \"baseInfo\".CID = ").append(getCurrentCompanyId());
	//					} else if(type == Sql.TYPE_LIST_REFERENCE){
	//						if(null != crossCompanyFlag && !crossCompanyFlag){
	//							s.append(" AND \"baseInfo\".CID = ").append(getCurrentCompanyId());
	//						}
	//					}
	//				}
					s.append(" ) ");
			}
			// ////PowerCondition

			//组合自定义条件
			String customCondition=getCustomCondition(page, viewCode, type, processKey, params,list);
			if (customCondition!=null&&customCondition.trim().length() > 0) {
				if (hasWhere){
					s.append(" AND ");
				}else{
					s.append(" WHERE (");
					hasWhere = true;
				}
					
				s.append(customCondition);
			}
			
			//一个实体只有一个权限操作
			if(permissionCode == null || permissionCode.length() == 0) {
				permissionCode = viewCode;
			}
			View view = viewServiceFoundation.getView(viewCode);
			ExtraView extraView = null;
			boolean isTreeView=false;
			boolean needPermission = true;
			if(view.getType() == ViewType.REFERENCE || view.getType() == ViewType.REFTREE){
				if(view.getShowType().equals(ShowType.PART)){
					//取布局视图
					View layoutView = viewServiceFoundation.getView(permissionCode);
					if(null != layoutView && !layoutView.getIsPermission()){//未启用权限
						needPermission = false;
					}
					if(null == layoutView) {
						layoutView = getLayoutView(permissionCode);
						if(null != layoutView && !layoutView.getIsPermission()){//未启用权限
							needPermission = false;
						}	
					}
				} else {
					if(null != view && !view.getIsPermission()){//未启用权限
						needPermission = false;
					}
				}
			}
			//加入权限过滤
			if(needPermission){
				String powerCode = permissionCode + "_self";
				if(view.getType() == ViewType.REFERENCE || view.getType() == ViewType.REFTREE){
					if(view.getShowType().equals(ShowType.PART)){
						View layoutView = viewServiceFoundation.getView(permissionCode);
						if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
							powerCode = permissionCode;
						}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
							powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
						}
						if(null == layoutView)  {
							layoutView = getLayoutView(permissionCode);
							if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
									powerCode = permissionCode;
							}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
									powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
							}
						}
					}else  {
						if(view.getIsPermission() && view.getPermissionCode().trim().length() > 0){
							powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + view.getPermissionCode();
						}
					}
				}else if(null != dgCode && !"".equals(dgCode)){
					DataGrid dataGrid = viewServiceFoundation.getDataGrid(dgCode);
					if(null != dataGrid.getDataGridType() && dataGrid.getDataGridType() == 1 && null != dataGrid.getIsPermission() && dataGrid.getIsPermission()){
						powerCode = dataGrid.getTargetModel().getCode() + "_" + dataGrid.getPermissionCode();
					}
				}
				String powerCodeSql = "select m.code as code from base_MenuOperate m where m.code = ? and m.valid = 1";
				List<Object> powerCodeList =  baseInfoDao.createNativeQuery(powerCodeSql, powerCode).list();
				if(powerCodeList.size() > 0) {
					powerCode = powerCodeList.get(0).toString();
				} else {
					String powerOperateSql = "select m.code as code from base_MenuOperate m where m.entity_Code=? and m.valid=1 and Power_Flag=1";
					List<Object> checkList =  baseInfoDao.createNativeQuery(powerOperateSql, "BEAM_1.0.0_baseInfo").list();
					if(checkList.size()>0){
						powerCode = checkList.get(0).toString();
					}
				}	
					String	pc = dataPermissionService.getBaseModelPowerCondition(this.creatorService.getStaffFromSession().getUser(), powerCode, "\"baseInfo\"","BEAM_1.0.0_baseInfo",customCondition,"EAM_BaseInfo",false);
				if (pc.trim().length() > 0) {
					if (hasWhere){
						s.append(" AND ");
					}else{
						s.append(" WHERE (");
						hasWhere = true;
					}
						
					s.append(pc);
				}
			}
			
			if(null!=processKey&&!processKey.equals("")){
				if(type == Sql.TYPE_LIST_QUERY){
					
					if (hasWhere){
						s.append(" AND ");
					}else{
						s.append(" WHERE (");
						hasWhere = true;
					}
					String[] arr=processKey.split(",");
					String pendingSql="";
					for(String str:arr){
						if(!"".equals(pendingSql)){
							pendingSql+=" or D.PROCESS_KEY = ? ";
						}else{
							pendingSql+=" D.PROCESS_KEY = ? ";
						}
						list.add(str);
					}
					if(!"".equals(pendingSql)){
						s.append(" \"baseInfo\".TABLE_INFO_ID IN (  SELECT   D.TABLE_INFO_ID "+
	                      "   FROM   "+BEAMBaseInfoDealInfo.TABLE_NAME+" d "+
	                      "  WHERE   ("+pendingSql+") "+
	                      " GROUP BY   D.TABLE_INFO_ID) ");
					}
					
				}else if(type==Sql.TYPE_LIST_PENDING){
					if (hasWhere){
						s.append(" AND ");
					}else{
						s.append(" WHERE (");
						hasWhere = true;
					}
					
					String[] arr=processKey.split(",");
					String pendingSql="";
					for(String str:arr){
						if(!"".equals(pendingSql)){
							pendingSql+=" or  \"p\".PROCESS_KEY = ? ";
						}else{
							pendingSql+=" \"p\".PROCESS_KEY = ? ";
						}
						list.add(str);
					}
					if(!"".equals(pendingSql)){
						s.append(" ("+pendingSql+")");
					}
				}
				
			}
			if(s != null && s.length()>0 && s.toString().contains("WHERE")){
				s.append(") ");
			}
			
			
			if (referenceCondition != null && referenceCondition.length() > 0) {
				s.append(" AND (").append(referenceCondition).append(")");
			}
			if(queryType == 1) {
				s.append(" AND \"baseInfo\".STATUS = 99 ");
			}
			// 开始处理排序，避免在统计时带入排序条件
			StringBuilder orderPart = new StringBuilder();
			orderPart.append(" ORDER BY ");
			String colOrderByStr = null;
			if (null != dgCode && !"".equals(dgCode)) {
				colOrderByStr = sqlService.getDGSqlQuery(dgCode,Sql.TYPE_USED_ORDERBY);
			} else {
				colOrderByStr = sqlService.getSqlQuery(viewCode,Sql.TYPE_USED_ORDERBY);
			}
			if(null != colOrderByStr && colOrderByStr.length() > 0) {
				if(sortOrderByStr.toString().length() > 0) {
					String[] colOrderByArr = colOrderByStr.substring(1).split(",");
					String[] sortOrderByArr = sortOrderByStr.toString().split(" ");
					Boolean flag = false;
					for(String colOrderBy : colOrderByArr) {
						if(colOrderBy.indexOf(sortOrderByArr[0] + " ") > -1) {
							colOrderByStr = colOrderByStr.replace(colOrderBy, sortOrderByStr.toString());
							flag = true;
							break;
						}
					}
					if(!flag) {
						orderPart.append(sortOrderByStr).append(",");
					} 
					orderPart.append(colOrderByStr.substring(1)).append(",");
				} else {
					orderPart.append(colOrderByStr.substring(1)).append(",");
				}
			} else {
				if(sortOrderByStr.toString().length() > 0) {
					orderPart.append(sortOrderByStr.toString()).append(",");
				} 
			}
			if(type == Sql.TYPE_LIST_PENDING) {
				orderPart.append(" \"p\".ID DESC");
			} else {
				if(isTreeView) {
					orderPart.append(" \"baseInfo\".LAY_REC ASC, \"baseInfo\".SORT ASC");
				} else {
					orderPart.append(" \"baseInfo\".EAM_ID DESC");
				}
			}
			Object[] arr = list.toArray();
			totalSql.append(s);
			String realSql = totalSql.toString() + orderPart.toString();
			Map<String, String> maps = new HashMap<String, String>();
			countSql += " ( " + totalSql.toString() + " ) T";
			// 突破ORACLE 30个字符限制
			if(DbUtils.getDbName().equals("oracle")){
			realSql = matchSql(pattern, realSql, "T", maps, 2, 4);
			countSql = replaceSql(pattern, countSql, maps, 4, 2);
			realSql = matchSql(p, realSql, "E", null, 4, 6);
			countSql = matchSql(p, countSql, "E", null, 4, 6);
			//组织总条数，合计SQL
			countSql = replaceSql(countPattern, countSql, maps, 4, 2);
			if(isTreeView) {
				page.setPaging(false);
			}
			}
			//计算条数
			if (page.needCount()) {
				//String countSql = "SELECT COUNT(*) FROM (" + realSql + ")";
				//Long count = ((Number) baseInfoDao.createNativeQuery(countSql, arr).uniqueResult()).longValue();
				Long count = 0l;
			    Map<String, BigDecimal> resultTotals = new HashMap<String, BigDecimal>();
			    SQLQuery query=baseInfoDao.createNativeQuery(countSql, arr);
			    if(customerSqlListMap!=null&&!customerSqlListMap.isEmpty()){
					for(String key:customerSqlListMap.keySet()){
						query.setParameterList(key, (Object[])customerSqlListMap.get(key));
					}
				}
			    List<Map<String, Object>> resultCountList = (List<Map<String, Object>>)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			    if (null != resultCountList && !resultCountList.isEmpty()) {
					Map<String, Object> resultCounts = resultCountList.get(0);
					for (Map.Entry<String, Object> entry : resultCounts.entrySet()) {
						String key = entry.getKey();
						if(maps.containsKey(key)) {
							key = (String) maps.get(key);
						}
						if (key.equalsIgnoreCase("count")) {
							count = ((Number) (null == entry.getValue() ? 0 : entry.getValue())).longValue();
						} else {
							resultTotals.put(key, BigDecimal.valueOf(((Number) (null == entry.getValue() ? 0 : entry.getValue())).doubleValue()));
						}
					}
			    }
			    if(!page.isExportFlag() && !page.isPaging() && count > page.getMaxPageSize()) {
			    	throw new BAPException(BAPException.Code.RESULT_COUNT_EXCEED_MAX_PAGE_SIZE);
			    }
			    page.setTotalCount(count);
			    page.setResultTotals(resultTotals);
			}
			if(!noQueryFlag)  {
				SQLQuery query = baseInfoDao.createNativeQuery(realSql, arr);
				if(customerSqlListMap!=null&&!customerSqlListMap.isEmpty()){
					for(String key:customerSqlListMap.keySet()){
						query.setParameterList(key, (Object[])customerSqlListMap.get(key));
					}
				}
				List<BEAMBaseInfo> result = new ArrayList<BEAMBaseInfo>();
				if(page.isExportFlag()) {  
					result = getResult(page, query, new PendingResultTransformer(BEAMBaseInfo.class,baseInfoDao,maps,true));
				}else {
					result = getResult(page, query, new PendingResultTransformer(BEAMBaseInfo.class,baseInfoDao,maps));
				}
				modelServiceFoundation.initCacheData();
				page.setResult(result);
				if(null != hasAttachment && hasAttachment) {
					for (BEAMBaseInfo baseInfo : result) {
						long count = 0l;
						count = documentService.getCountByLinkIdAndType(baseInfo.getId(), "BEAM_baseInfo_baseInfo");
						if(count > 0) {
							List<Document> documents = documentService.getByLinkIdAndType(baseInfo.getId(), "BEAM_baseInfo_baseInfo");
							if(null != documents && !documents.isEmpty()) {
								baseInfo.setDocument(documents.get(0));
								baseInfo.setBapAttachmentInfo(documents.get(0).getName() + "@_@BAP@_@更多(" + count + ")");
							}
						}
					}
				}
				
				
				for (BEAMBaseInfo baseInfo : result) {
				}	
				if(exportSql.trim().isEmpty()){
					getConfigAssoPropsResult(viewCode, dgCode, result); // 获取配置的关联模型字段的结果集
				}
			}
		}
	}
	
	
	private View getLayoutView(String permissionCode) {
		if(null!=permissionCode&&permissionCode.indexOf("BEAM_1.0.0_baseInfo")!=-1) {
				String personalCode=permissionCode.substring(permissionCode.indexOf("BEAM_1.0.0_baseInfo")+"BEAM_1.0.0_baseInfo".length()+1);
				if(null!=personalCode&&personalCode.trim().length()>0)  {
						return viewServiceFoundation.getViewWithPermissionCode("BEAM_1.0.0_baseInfo",personalCode);
				}
		}
		return null;
	}
	
	private void getConfigAssoPropsResult(String viewCode, List<BEAMBaseInfo> result) {
		getConfigAssoPropsResult(viewCode, null, result);
	}
	
	private void getConfigAssoPropsResult(String viewCode,String dataGridCode, List<BEAMBaseInfo> result) {
		View view = null;
		DataGrid dataGrid = null;
		List<Field> fields = null;
		Model origModel = null;
		if (null != dataGridCode && !"".equals(dataGridCode)) {
			dataGrid = viewServiceFoundation.getDataGrid(dataGridCode);
			fields = viewServiceFoundation.getFields(dataGrid);
			origModel = dataGrid.getTargetModel();
		} else {
			view = viewServiceFoundation.getView(viewCode);
			fields = viewServiceFoundation.getFields(view);
			origModel = view.getAssModel();
		}
		String origId = modelServiceFoundation.getPropertyColumnName(origModel, "id", false);

		List<Long> ids = new ArrayList<Long>();
		Map<Long, BEAMBaseInfo> rsMap = new HashMap<Long, BEAMBaseInfo>();
		for (BEAMBaseInfo baseInfo : result) {
			ids.add(baseInfo.getId());
			rsMap.put(baseInfo.getId(), baseInfo);
		}
		for (Field f : fields) {
			if (f.getRegionType() == RegionType.LISTPT && (f.getCode().startsWith(viewCode + "_LISTPT_ASSO_") || f.getCode().startsWith(dataGridCode + "_LISTPT_ASSO_"))) { // 关联模型字段
				Map<String, Object> map = (Map<String, Object>) SerializeUitls.deserialize(f.getConfig());
				Map<String, Object> fieldMap = (Map<String, Object>) map.get("field");
				if (fieldMap.get("assoFlag") != null && "true".equalsIgnoreCase(fieldMap.get("assoFlag").toString())) {
					String assoConfig = (String) fieldMap.get("assoConfig");
					Map<String, Object> configMap = (Map<String, Object>) SerializeUitls.deserialize(assoConfig);
					if (configMap != null && configMap.size() > 0) {
						String sepBegin1 = configMap.get("separatorBeginLevel1") == null ? "" : configMap.get("separatorBeginLevel1").toString();
						String sepEnd1 = configMap.get("separatorEndLevel1") == null ? "" : configMap.get("separatorEndLevel1").toString();
						String sepBegin2 = configMap.get("separatorBeginLevel2") == null ? "" : configMap.get("separatorBeginLevel2").toString();
						String sepEnd2 = configMap.get("separatorEndLevel2") == null ? "" : configMap.get("separatorEndLevel2").toString();

						List<Map<String, Object>> configList = (List<Map<String, Object>>) configMap.get("config");
						Map<String, Object> cfg1 = configList.get(0);
						String propertyCode1 = (String) cfg1.get("propertyCode");
						String[] propArr1 = propertyCode1.split("\\|\\|");
						Property ftProp1 = modelServiceFoundation.getProperty(propArr1[0]);
						Property prop1 = findAssoProperty(ftProp1.getModel().getCode(), origModel.getCode());
						String id1 = modelServiceFoundation.getPropertyColumnName(prop1.getModel(), "id", false);

						Property prop2 = null;
						String id2 = null;
						for (Map<String, Object> cfgMap : configList) {
							String level = String.valueOf(cfgMap.get("level"));
							if ("2".equals(level)) {
								String propertyCode2 = (String) cfgMap.get("propertyCode");
								String[] propArr2 = propertyCode2.split("\\|\\|");
								Property ftProp2 = modelServiceFoundation.getProperty(propArr2[0]);
								prop2 = findAssoProperty(ftProp2.getModel().getCode(), prop1.getModel().getCode());
								id2 = modelServiceFoundation.getPropertyColumnName(prop2.getModel(), "id", false);
								break;
							}
						}
						List<List<Map<String, Object>>> list1 = new ArrayList<List<Map<String, Object>>>();
						List<List<Map<String, Object>>> list2 = new ArrayList<List<Map<String, Object>>>();
						String lastSep2Level1 = "", lastSep2Level2 = "";
						for (int index = 0; index < configList.size(); index++) {
							Map<String, Object> cfg = configList.get(index);
							String level = String.valueOf(cfg.get("level"));
							String sep1 = cfg.get("separator1") == null ? "" : cfg.get("separator1").toString();
							String sep2 = cfg.get("separator2") == null ? "" : cfg.get("separator2").toString();
							String method = cfg.get("method") == null ? "" : cfg.get("method").toString().toUpperCase();
							String propertyCode = (String) cfg.get("propertyCode");
							String propertyType = String.valueOf(cfg.get("propertyType"));
							StringBuilder fromSql = new StringBuilder(" from ");
							StringBuilder whereSql = new StringBuilder(" where ");
							if ("1".equals(level)) {
								lastSep2Level1 = sep2;
								fromSql.append(prop1.getModel().getTableName());
								whereSql.append(prop1.getModel().getTableName()).append(".").append("VALID = 1");
							} else {
								lastSep2Level2 = sep2;
								fromSql.append(prop2.getModel().getTableName());
								whereSql.append(prop2.getModel().getTableName()).append(".").append("VALID = 1");
							}
							String[] propArr = propertyCode.split("\\|\\|");
							for (int i = 0; i < propArr.length - 1; i++) {
								Property tmpProp = modelServiceFoundation.getProperty(propArr[i]);
								Model tmpModel = tmpProp.getModel();
								Property tmpAssoProp = tmpProp.getAssociatedProperty();
								Model tmpAssoModel = tmpAssoProp.getModel();
								fromSql.append(" left join ").append(tmpAssoModel.getTableName());
								fromSql.append(" on ").append(tmpModel.getTableName()).append(".").append(tmpProp.getColumnName()).append(" = ")
										.append(tmpAssoModel.getTableName()).append(".").append(tmpAssoProp.getColumnName());
								whereSql.append(" and ").append(tmpAssoModel.getTableName()).append(".").append("VALID = 1").append(" ");
							}

							String selectSql = "select " + origModel.getTableName() + "." + origId + " AS OID";
							Property laProp = modelServiceFoundation.getProperty(propArr[propArr.length - 1]);
							if (!"".equals(method)) {
								if ("2".equals(level)) {
									selectSql += "," + prop1.getModel().getTableName() + "." + id1 + " AS ID1";
								}
								if ("COUNT".equalsIgnoreCase(method)) {
									Property laPkProperty = null;
									for (Property p : laProp.getModel().getProperties()) {
										if (p.getIsPk() != null && p.getIsPk()) {
											laPkProperty = p;
											break;
										}
									}
									selectSql += ", " + method + " (" + laProp.getModel().getTableName() + "." + laPkProperty.getColumnName() + ") AS VAL";
								} else {
									selectSql += ", " + method + " (" + laProp.getModel().getTableName() + "." + laProp.getColumnName() + ") AS VAL";
								}
							} else {
								selectSql += ", " + prop1.getModel().getTableName() + "." + id1 + " AS ID1";
								if ("2".equals(level)) {
									selectSql += ", " + prop2.getModel().getTableName() + "." + id2 + " AS ID2";
								}
								selectSql += ", " + laProp.getModel().getTableName() + "." + laProp.getColumnName() + " AS VAL";
								if("SYSTEMCODE".equals(propertyType)){
									selectSql += ", BASE_SYSTEMCODE.VALUE AS REALVAL";
								}
							}

							if ("2".equals(level)) {
								fromSql.append(" left join ").append(prop1.getModel().getTableName()).append(" on ").append(prop2.getModel().getTableName())
										.append(".").append(prop2.getColumnName()).append(" = ").append(prop1.getModel().getTableName()).append(".")
										.append(prop2.getAssociatedProperty().getColumnName());
								whereSql.append(" and ").append(prop1.getModel().getTableName()).append(".").append("VALID = 1");
							}
							fromSql.append(" left join ").append(origModel.getTableName()).append(" on ").append(prop1.getModel().getTableName()).append(".")
									.append(prop1.getColumnName()).append(" = ").append(origModel.getTableName()).append(".")
									.append(prop1.getAssociatedProperty().getColumnName());
							if("SYSTEMCODE".equals(propertyType)){
								fromSql.append(",BASE_SYSTEMCODE");
							}
							whereSql.append(" and ").append(origModel.getTableName()).append(".").append("VALID = 1");
							whereSql.append(" and ").append(origModel.getTableName()).append(".").append(origId).append(" in (:ids)");
							if("SYSTEMCODE".equals(propertyType)){
								whereSql.append(" and ").append(laProp.getModel().getTableName() + "." + laProp.getColumnName()).append("= BASE_SYSTEMCODE.ID");
							}
							String orderSql = " order by ";
							if (!"".equals(method)) {
								whereSql.append(" group by ").append(origModel.getTableName() + "." + origId);
								if ("2".equals(level)) {
									whereSql.append(", ").append(prop1.getModel().getTableName() + "." + id1);
								} 
								orderSql += "2".equals(level) ? "ID1 ASC" : "OID ASC";
							} else {
								orderSql += "2".equals(level) ? "ID2 ASC" : "ID1 ASC";
							}
							String sql = selectSql + fromSql.toString() + whereSql.toString() + orderSql;

							List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
							final int PERTIME = 999;
							int count = (ids.size() / PERTIME) + (ids.size() % PERTIME == 0 ? 0 : 1);
							for (int i = 0; i < count; i++) {
								rs.addAll(baseInfoDao.createNativeQuery(sql)
										.setParameterList("ids", ids.subList(PERTIME * i, PERTIME * i + (i < count - 1 ? PERTIME : (ids.size() % PERTIME))))
										.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
							}
							if (rs.size() > 0) {
								if("SYSTEMCODE".equals(propertyType)){
									for (Map<String, Object> m : rs) {
										String REALVAL = m.get("REALVAL") == null ? "" : String.valueOf(m.get("REALVAL"));
										m.put("VAL", sep1 + InternationalResource.get(REALVAL, getCurrentLanguage()) + sep2);
									}
								}else{
									for (Map<String, Object> m : rs) {
										if(null != m.get("VAL") && !"".equals(String.valueOf(m.get("VAL")))){
											String val = String.valueOf(m.get("VAL"));
											m.put("VAL", sep1 + val + sep2);
										}else{
											m.put("VAL", "");
										}										
									}
								}
								if ("1".equals(level)) {
									list1.add(rs);
								} else if ("2".equals(level)) {
									list2.add(rs);
								}
							}
						}
						List<Map<String, Object>> fList1 = concatListValue(list1);
						List<Map<String, Object>> fList2 = concatListValue(list2);
						if (fList2 != null) {
							for (Map<String, Object> tmpMap1 : fList1) {
								long tmpId1 = ((Number) tmpMap1.get("ID1")).longValue();
								StringBuilder val2 = new StringBuilder();
								val2.append(sepBegin2);
								for (Map<String, Object> tmpMap2 : fList2) {
									long tmpID2 = ((Number) tmpMap2.get("ID1")).longValue();
									if (tmpId1 == tmpID2) {
										val2.append((String) tmpMap2.get("VAL"));
									}
								}
								String val = val2.toString();
								if (!val.equals(sepBegin2)) {
									val = val.substring(0, val.length() - lastSep2Level2.length());
								}
								val += sepEnd2;
								tmpMap1.put("VAL", tmpMap1.get("VAL") + val);
							}
						}
						if (fList1 != null) {
							for (Long tabId : rsMap.keySet()) {
								StringBuilder val1 = new StringBuilder(sepBegin1);
								for (Map<String, Object> tmpMap1 : fList1) {
									long tmpTabId = ((Number) tmpMap1.get("OID")).longValue();
									if (tmpTabId == tabId) {
										val1.append((String) tmpMap1.get("VAL"));
									}
								}
								if (!val1.toString().equals(sepBegin1)) {
									String val = val1.toString();
									if (fList2 == null || fList2.size() < 1){
										val = val.substring(0, val.length() - lastSep2Level1.length());
									}
									val += sepEnd1;
									rsMap.get(tabId).setAttrObject(f.getCode().replace(".", "_"), val);
								}
							}
						}
					}
				}
			}
		}
	}

	private List<Map<String, Object>> concatListValue(List<List<Map<String, Object>>> list) {
		if (list.size() == 0) {
			return null;
		}
		List<Map<String, Object>> fList = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			List<Map<String, Object>> tmpList = list.get(i);
			for (int j = 0; j < tmpList.size(); j++) {
				String tmpVal = (String) tmpList.get(j).get("VAL");
				Map<String, Object> map = fList.get(j);
				String val = (String) map.get("VAL");
				map.put("VAL", val + tmpVal);
			}
		}
		return fList;
	}

	private Property findAssoProperty(String origModelCode, String assoModelCode) {
		String hql = "from Property p where p.valid = true and p.model.code = ? and p.associatedProperty.model.code = ? and (p.associatedType = ? or p.associatedType = ?)";
		List<Property> list = baseInfoDao.findByHql(hql, new Object[] {origModelCode, assoModelCode, Property.ONE_TO_ONE, Property.MANY_TO_ONE});
		return list.get(0);
	}
	

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDataGridPage(DataGrid dg,final Class dgClass,Page dgPage,final Object orgObj,String condition, List<Object> params){
		Map confMap=dg.getConfigMap();
		Map layoutMap = (Map) confMap.get("layout");
		Map propertyMap = (Map) layoutMap.get("listProperty");
		if(dg.getDataGridType() ==0 && Boolean.TRUE.equals(propertyMap.get("isTreeView"))){
			String conditionSql = (null != condition && condition.trim().length() > 0) ? " and " + condition : "";
			List<Object> list = new ArrayList<Object>();
			list.add(orgObj);
			if(condition != null && condition.trim().length() > 0 && params != null && params.size() > 0){
				list.addAll(params);
			} 
			//String key=dg.getTargetModel().getModelName()+fLTU(dg.getName())+((com.supcon.orchid.orm.entities.AbstractEcFullEntity)orgObj).getId();
			String orgObjId = "";
			try {
				Method method = orgObj.getClass().getMethod("getId");
				orgObjId = String.valueOf((Long) method.invoke(orgObj));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				log.error(e1.getMessage(), e1);
			}
			String key=dg.getTargetModel().getModelName()+fLTU(dg.getName())+orgObjId;
			String orgPropertyName=dg.getOrgProperty().getName();
			String targetModelName=fLTL(dg.getTargetModel().getModelName());
			List treeList = new ArrayList();
			Object[] daoparams=new Object[]{dgClass,key,orgPropertyName,targetModelName,conditionSql,list,new boolean[]{false}};
			try {
				Class thisclass=this.getClass();
				java.lang.reflect.Field daoField=thisclass.getDeclaredField(fLTL(dg.getTargetModel().getModelName())+"Dao");	
				java.lang.reflect.Field serviceField=thisclass.getDeclaredField(fLTL(dg.getTargetModel().getModelName())+"Service");	
				Class serviceType=serviceField.getType();
				Class daoType=daoField.getType();
				Object tree=daoType.getMethod("buildTree",Class.class,String.class,String.class,String.class,String.class,List.class,boolean[].class).invoke(daoField.get(this),daoparams);
				Tree.treeToList((com.supcon.orchid.tree.TreeNode)tree, treeList, false);
				treeList=(List)serviceType.getMethod("findByTreeDataGrid"+dg.getOrgProperty().getAssociatedProperty().getModel().getModelName(),new Class[]{Page.class}).invoke(serviceField.get(this),new Object[]{treeList});
			} catch (Exception e) {
				e.printStackTrace();
			}
			dgPage.setResult(treeList);
		}else{
			DetachedCriteria criteria = DetachedCriteria.forClass(dgClass);
			if(null != orgObj){
				criteria.add(Restrictions.eq(dg.getOrgProperty().getName(), orgObj));
			}
			criteria.add(Restrictions.eq("valid", true));
			criteria.addOrder(Order.asc("sort"));
			if(!"".equals(condition) && condition.trim().length() > 0) {
				criteria.add(Restrictions.sqlRestriction(condition.replace("\""+fLTL(dg.getTargetModel().getModelName())+"\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
			}
			try {
				Class thisclass=this.getClass();
				java.lang.reflect.Field daoField=thisclass.getDeclaredField(fLTL(dg.getTargetModel().getModelName())+"Dao");	
				Class daoType=daoField.getType();
				daoType.getMethod("findByPage",new Class[]{Page.class,DetachedCriteria.class}).invoke(daoField.get(this),new Object[]{dgPage,criteria});
				java.lang.reflect.Field serviceField=thisclass.getDeclaredField(fLTL(dg.getTargetModel().getModelName())+"Service");	
				Class serviceType=serviceField.getType();
				serviceType.getMethod("findByNormalDataGrid"+dg.getOrgProperty().getAssociatedProperty().getModel().getModelName(),new Class[]{Page.class}).invoke(serviceField.get(this),new Object[]{dgPage});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public String fLTL(String str){
		return com.supcon.orchid.utils.StringUtils.firstLetterToLower(str);
	}
	
	public String fLTU(String str){
		return com.supcon.orchid.utils.StringUtils.firstLetterToUpper(str);
	}
	
	protected EntityTableInfo getTableInfo(BEAMBaseInfo baseInfo,boolean... isImport) {
		EntityTableInfo ti = new EntityTableInfo();


		ti.setOwnerStaffId(baseInfo.getOwnerStaffId());
		ti.setOwnerPositionId(baseInfo.getOwnerPositionId());
		ti.setOwnerDepartmentId(baseInfo.getOwnerDepartmentId());
		
		ti.setCreateStaff(baseInfo.getCreateStaff());
		ti.setCreateStaffId(baseInfo.getCreateStaffId());
		ti.setCreateTime(baseInfo.getCreateTime());
		ti.setDeleteStaff(baseInfo.getDeleteStaff());
		ti.setDeleteStaffId(baseInfo.getDeleteStaffId());
		ti.setDeleteTime(baseInfo.getDeleteTime());
		ti.setCreateDepartmentId(baseInfo.getCreateDepartmentId());
		ti.setModifyStaff(baseInfo.getModifyStaff());
		ti.setModifyStaffId(baseInfo.getModifyStaffId());
		ti.setCreatePositionId(baseInfo.getCreatePositionId());
		ti.setPositionLayRec(baseInfo.getPositionLayRec());
		ti.setTableNo(baseInfo.getTableNo());
		ti.setValid(true);
		ti.setTargetEntityCode("BEAM_1.0.0_baseInfo");
		ti.setTargetTableName("EAM_BaseInfo");
		
		return ti;
	}
	
	public List<Object[]> mneCodeSearch( String viewCode,  int showNumber, boolean cross, List<Param> params){
		return mneCodeSearch(viewCode, showNumber, cross, params,null,null,null,null);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Object[]> mneCodeSearch( String viewCode,  int showNumber, boolean cross, List<Param> params,String refViewCode,String currentViewCode,String sqlType,String realPermissionCode){
		List<Object[]> resultList = null;
		List<Object> totalValues = new ArrayList<Object>();
		String customerSql = null;
		List<Object> customerSqlValues = null;
		String customerCondition = null;
		List<Object> customerValues = null;
		Object mneValues = null;
		Object notlikeValue = null;
		for(Param p : params){
			
			if("customerSql".equals(p.getName())){
				customerSql = (String) p.getValue();
				continue;
			}
			
			if("customerSqlValues".equals(p.getName())){
				customerSqlValues = (List<Object>) p.getValue();
				continue;
			}
			
			if("customerCondition".equals(p.getName())){
				customerCondition = (String) p.getValue();
				continue;
			}

			if("customerValues".equals(p.getName())){
				customerValues = (List<Object>) p.getValue();
				continue;
			}
			
			
			if("mneValues".equals(p.getName())){
				mneValues =  (Object) p.getValue();
				continue;
			}
			
			if("notlikeValue".equals(p.getName())){
				notlikeValue =  (Object) p.getValue();
				continue;
			}
		}
		
		String sql = sqlService.getSqlQuery(viewCode, Sql.TYPE_USED_MNECODE);
		if (null != sql && sql.length() > 0) {
			StringBuilder mnecodeSql = new StringBuilder();
			mnecodeSql.append(sql);
			//助记码权限,一个实体只有一个权限操作
			String  powerSql="";
			Boolean searchRefView=false;
			Boolean innerJoinAppendFlag = false;
			String  permissionCode = viewCode;
			if(realPermissionCode!=null&&!realPermissionCode.isEmpty())  {
				permissionCode = realPermissionCode;
			}
			StringBuilder sb=new StringBuilder();
			if(refViewCode!=null&&!refViewCode.equals(""))  {
				View refView=viewServiceFoundation.getView(refViewCode);
				//判断是否有参照视图,没有则抛出异常
				if(null==refView)  {
					throw new BAPException(BAPException.Code.REFERVIEW_NOT_FOUND,refViewCode);
				}	
				//未启用权限,使用主列表视图的权限
				String powerCode = permissionCode + "_self";
				boolean needPermission = true;
				if(refView.getType() == ViewType.REFERENCE || refView.getType() == ViewType.REFTREE){
					if(refView.getShowType().equals(ShowType.PART)){
						//取布局视图
						View layoutView = viewServiceFoundation.getView(permissionCode);
						if(null != layoutView && !layoutView.getIsPermission()){//未启用权限
							needPermission = false;
						}
						if(null == layoutView) {
							layoutView = getLayoutView(permissionCode);
							if(null != layoutView && !layoutView.getIsPermission()){//未启用权限
								needPermission = false;
							}	
						}
					} else {
						if(null != refView && !refView.getIsPermission()){//未启用权限
							needPermission = false;
						}
					}
				}
				if(needPermission)  {
					if(null == refView ){
						powerCode = permissionCode;
					}else if(refView.getShowType().equals(ShowType.PART))  {
						View layoutView = viewServiceFoundation.getView(permissionCode);
						if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
							powerCode = permissionCode;
						}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
							powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
							searchRefView=true;
						}
						if(null == layoutView)  {
							layoutView = getLayoutView(permissionCode);
							if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
									powerCode = permissionCode;
							}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
									powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
							}
						}
					} else if(refView.getIsPermission() && refView.getPermissionCode().trim().length() > 0){
						powerCode ="BEAM_1.0.0_baseInfo_BaseInfo" + "_" + refView.getPermissionCode();
						searchRefView=true;
					}
					String powerCodeSql = "select m.code as code from base_MenuOperate m where m.code = ? and m.valid = 1";
					List<Object> powerCodeList =  baseInfoDao.createNativeQuery(powerCodeSql, powerCode).list();
					if(powerCodeList.size() > 0) {
						powerCode = powerCodeList.get(0).toString();
					} else {
						String powerOperateSql = "select m.code as code from base_MenuOperate m where m.entity_Code=? and m.valid=1 and Power_Flag=1";
						List<Object> checkList =  baseInfoDao.createNativeQuery(powerOperateSql, "BEAM_1.0.0_baseInfo").list();
						if(checkList.size()>0){
							powerCode = checkList.get(0).toString();
						}
					}
						powerSql  = dataPermissionService.getBaseModelPowerCondition(this.creatorService.getStaffFromSession().getUser(), powerCode, "\"baseInfo\"","BEAM_1.0.0_baseInfo","","EAM_BaseInfo",false);
					//if(!searchRefView&&refView.getAssModel().getEntity().getWorkflowEnabled())  {
						//查询主列表视图的权限(表单类型)
						//	sb.append(" INNER JOIN WF_PENDING \"p\" ON \"p\".TABLE_INFO_ID=\"baseInfo\".TABLE_INFO_ID ");
						//	sb.append(" WHERE  \"baseInfo\".STATUS <> 0  ");
					//}
					//powerSql = dataPermissionService.getBaseModelPowerCondition(this.creatorService.getStaffFromSession().getUser(), powerCode, "\"baseInfo\"","BEAM_1.0.0_baseInfo","","EAM_BaseInfo",false);
					if(sb.length()>0) {
						sb.append(" AND ");
					}
					sb.append(powerSql);
				}
			}else {
				//如果未关联参照视图则使用当前列表的查询权限
				if(currentViewCode!=null&&currentViewCode.trim().length()>0)  {
					boolean needPermission=true;
					View view=viewServiceFoundation.getView(currentViewCode);
					String powerCode = permissionCode + "_self";
					if(view.getType() == ViewType.REFERENCE || view.getType() == ViewType.REFTREE){
						searchRefView=true;
						if(view.getShowType().equals(ShowType.PART)){
							View layoutView = viewServiceFoundation.getView(permissionCode);
							if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
								powerCode = permissionCode;
								needPermission=false;
							}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
								powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
							}
							if(null == layoutView)  {
								layoutView = getLayoutView(permissionCode);
								if(null == layoutView  || !(layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0)){
									powerCode = permissionCode;
								}else if((layoutView.getIsPermission() && layoutView.getPermissionCode().trim().length() > 0))  {
									powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + layoutView.getPermissionCode();
									needPermission=true;
								}
							}
						}else  {
							if(view.getIsPermission() && view.getPermissionCode().trim().length() > 0){
								powerCode = "BEAM_1.0.0_baseInfo_BaseInfo" + "_" + view.getPermissionCode();
							}else {
								needPermission=false;
							}
						}
					}
					if(needPermission)  {
						String powerCodeSql = "select m.code as code from base_MenuOperate m where m.code = ? and m.valid = 1";
						List<Object> powerCodeList =  baseInfoDao.createNativeQuery(powerCodeSql, powerCode).list();
						if(powerCodeList.size() > 0) {
							powerCode = powerCodeList.get(0).toString();
						} else {
							String powerOperateSql = "select m.code as code from base_MenuOperate m where m.entity_Code=? and m.valid=1 and Power_Flag=1";
							List<Object> checkList =  baseInfoDao.createNativeQuery(powerOperateSql, "BEAM_1.0.0_baseInfo").list();
							if(checkList.size()>0){
								powerCode = checkList.get(0).toString();
							}
						}	
							powerSql  = dataPermissionService.getBaseModelPowerCondition(this.creatorService.getStaffFromSession().getUser(), powerCode, "\"baseInfo\"","BEAM_1.0.0_baseInfo","","EAM_BaseInfo",false);
						if(sb.length()>0) {
							sb.append(" AND ");
						}
						sb.append(powerSql);
					}
				}
				
			}
		
			
			
			if(!innerJoinAppendFlag)  {
				if(!sb.toString().trim().startsWith("WHERE"))  {
					mnecodeSql.append(" WHERE ");
				}
			}
			if(sb != null && sb.length() > 0 && !sb.toString().trim().endsWith(" AND")) {
				sb.append(" AND ");
			}
			mnecodeSql.append(sb);
			
			mnecodeSql.append("  (\"").append("baseInfo.mc\"").append(".MNE_CODE) like (?) escape '&' AND \"baseInfo\".VALID = 1");
			totalValues.add(mneValues);
			
			if(customerSql != null && customerSql.length() > 0){
				mnecodeSql.append(" AND (").append(customerSql).append(")");
				if(customerSqlValues!=null&&customerSqlValues.size()>0){
					totalValues.addAll(customerSqlValues);
				}
			}
			
			if(customerCondition != null && customerCondition.length() > 0){
				mnecodeSql.append(" AND (").append(customerCondition).append(")");
				if(customerValues!=null&&customerValues.size()>0){
					totalValues.addAll(customerValues);
				}
			}
			
			if(!cross){
				if(isSingleMode){
					mnecodeSql.append(" AND \"baseInfo\".CID = ?");
					totalValues.add(getCurrentCompanyId());
				}else{
					//当前公司不是集团情况下查询集团和本公司数据
					if(!getCurrentCompanyId().equals(1L)){
						mnecodeSql.append(" AND (\"baseInfo\".CID = 1 OR \"baseInfo\".CID = ?)");
						totalValues.add(getCurrentCompanyId());
					}
				}
			}
			
			//组合自定义条件
			String customCondition=getCustomMneCondition(viewCode,  showNumber, cross,  params, refViewCode, currentViewCode, sqlType, realPermissionCode);
			if (customCondition!=null&&customCondition.trim().length() > 0) {
				
				if(customCondition != null && customCondition.length() > 0 && !customCondition.toString().trim().startsWith("AND") ) {
					mnecodeSql.append(" AND ");
				}
				mnecodeSql.append(customCondition);
			}
			
			if(notlikeValue != null){
				mnecodeSql.append(" and (\"").append("baseInfo.mc\"").append(".MNE_CODE) not like (?) escape '&'");
				totalValues.add(notlikeValue);
			}
			
			SQLQuery query = baseInfoDao.createNativeQuery(mnecodeSql.toString(), totalValues.toArray(new Object[]{}));
			resultList = query.setMaxResults(showNumber).list();
		}
		
		return resultList;
		
	}

	@Override
	public void destroy() throws Exception {
		beforeServiceDestroy();
		if(this.codeCounter != null) {
			this.codeCounter.destroy();
		}
	   	aclPermissionService = null;
		sqlService = null;
		eventService = null;
		scriptService = null;
		documentService = null;
		authorityDepartmentDao = null;
		authorityDepartmentService = null;
		powerAreaDao = null;
		powerAreaService = null;
		powerDeptDao = null;
		powerDeptService = null;
		powerDutyStaffDao = null;
		powerDutyStaffService = null;
		powerEAMDao = null;
		powerEAMService = null;
		powerEamTypeDao = null;
		powerEamTypeService = null;
		powerHeadDao = null;
		powerHeadService = null;
		powerRoleDao = null;
		powerRoleService = null;
		powerStaffDao = null;
		powerStaffService = null;
		powerTypeDao = null;
		powerTypeService = null;
		lubricatingPartDao = null;
		lubricatingPartService = null;
		attachPartDao = null;
		attachPartService = null;
		baseCheckDao = null;
		baseCheckService = null;
		baseCheckItemDao = null;
		baseCheckItemService = null;
		baseInfoDao = null;
		baseinfoParamDao = null;
		baseinfoParamService = null;
		dealInfoTableDao = null;
		dealInfoTableService = null;
		delayRecordsDao = null;
		delayRecordsService = null;
		docPartDao = null;
		docPartService = null;
		jWXItemDao = null;
		jWXItemService = null;
		jwxRuleBeamDao = null;
		jwxRuleBeamService = null;
		jwxRuleHeadDao = null;
		jwxRuleHeadService = null;
		reportInfoDao = null;
		reportInfoService = null;
		sparePartDao = null;
		sparePartService = null;
		spareRecordDao = null;
		spareRecordService = null;
		workRecordDao = null;
		workRecordService = null;
		candidateValueDao = null;
		candidateValueService = null;
		inputStandardDao = null;
		inputStandardService = null;
		groupComponentDao = null;
		groupComponentService = null;
		repairGroupDao = null;
		repairGroupService = null;
		changeDao = null;
		changeService = null;
		changePartDao = null;
		changePartService = null;
		customPropertyModelDao = null;
		customPropertyModelService = null;
		eamTypeDao = null;
		eamTypeService = null;
		lubricateOilDao = null;
		lubricateOilService = null;
		busitypeDao = null;
		busitypeService = null;
		baseInfoWorkflowDao = null;
		baseInfoWorkflowService = null;
		updateInfoDao = null;
		updateInfoService = null;
		areaDao = null;
		areaService = null;
	
		dataPermissionService = null;
		counterManager = null;
		conditionService = null;
		viewServiceFoundation = null;
		creatorService = null;
		bundleContext = null;
		synchronizeInfoService = null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.beforeInitBean();
		this.counter = this.counterManager.addCounter("baseInfo", "BEAM_1.0.0_baseInfo", "baseInfo_{1,date,yyyyMMdd}_{0,number,000}", CounterType.Daily);
		String formatStr = "";
			//新的编码字段不需要初始化pattern
		this.codeCounter = this.counterManager.addCounter("BEAM_1.0.0_baseInfo_BaseInfo_code", "BEAM_1.0.0_baseInfo", formatStr, CounterType.All);

		this.afterInitBean();
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String getWorkFlowInfo(String menuCode) {
		String sql="select d.PROCESS_KEY PROCESSKEY from WF_DEPLOYMENT d,BASE_MENUINFO m where d.MENU_INFO_ID=m.id and d.valid=1 and m.code=? group by d.PROCESS_KEY";
		SQLQuery query=baseInfoDao.createNativeQuery(sql, menuCode);
		List<String> list=query.list();
		String keys="";
		for(String key:list){
			keys+=","+key;
		}
		if(!"".equals(keys)){
			keys=keys.substring(1);
		}
		
		return keys;
	}
	// ================ 生成多选控件数据保存方法 start====================
	private void dealEamIDStaffID(BEAMBaseInfo baseInfo) {
		String addIds = baseInfo.getEamIDStaffIDAddIds();
		String delIds = baseInfo.getEamIDStaffIDDeleteIds();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("staffID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		if (addIds != null && addIds.length() > 0) {
			String[] ids = addIds.split(",");
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					BEAMBaseCheck tmp = new BEAMBaseCheck();
					tmp.setEamID(baseInfo);
					Staff indirectObj = new Staff();
					indirectObj.setId(Long.valueOf(item));
					tmp.setStaffID(indirectObj);
					tmp.setCid(baseInfo.getCid());
					baseCheckDao.save(tmp);
				}
			}
		}
	}

	private void setEamIDStaffID(BEAMBaseInfo baseInfo) {
		setEamIDStaffID(baseInfo, null);
	}

	private void setEamIDStaffID(BEAMBaseInfo baseInfo, String viewCode) {
		List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Order.asc("id"), Restrictions.eq("eamID", baseInfo), Restrictions.eq("valid", true));
		String ids = "";
		String names = "";
		for (BEAMBaseCheck item : tmpList) {
			if(item.getStaffID() != null) {
				if(ids.length() > 0) {
					ids += ",";
					names += ",";
				}
				ids += item.getStaffID().getId();
				String displayField = "name";
				if(viewCode != null) {
					switch (viewCode) {
					}
				}
				names += ReflectUtils.getPropertyValue(item.getStaffID(), displayField);
			}
		}
		baseInfo.setBaseCheckList(tmpList);
		baseInfo.setEamIDStaffIDmultiselectIDs(ids);
		baseInfo.setEamIDStaffIDmultiselectNames(names);
	}
	private void dealEamIDBusiType(BEAMBaseInfo baseInfo) {
		String addIds = baseInfo.getEamIDBusiTypeAddIds();
		String delIds = baseInfo.getEamIDBusiTypeDeleteIds();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("busiType.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		if (addIds != null && addIds.length() > 0) {
			String[] ids = addIds.split(",");
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					BEAMBaseCheck tmp = new BEAMBaseCheck();
					tmp.setEamID(baseInfo);
					BEAMBusitype indirectObj = new BEAMBusitype();
					indirectObj.setId(Long.valueOf(item));
					tmp.setBusiType(indirectObj);
					tmp.setCid(baseInfo.getCid());
					baseCheckDao.save(tmp);
				}
			}
		}
	}

	private void setEamIDBusiType(BEAMBaseInfo baseInfo) {
		setEamIDBusiType(baseInfo, null);
	}

	private void setEamIDBusiType(BEAMBaseInfo baseInfo, String viewCode) {
		List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Order.asc("id"), Restrictions.eq("eamID", baseInfo), Restrictions.eq("valid", true));
		String ids = "";
		String names = "";
		for (BEAMBaseCheck item : tmpList) {
			if(item.getBusiType() != null) {
				if(ids.length() > 0) {
					ids += ",";
					names += ",";
				}
				ids += item.getBusiType().getId();
				String displayField = "name";
				if(viewCode != null) {
					switch (viewCode) {
					}
				}
				names += ReflectUtils.getPropertyValue(item.getBusiType(), displayField);
			}
		}
		baseInfo.setBaseCheckList(tmpList);
		baseInfo.setEamIDBusiTypemultiselectIDs(ids);
		baseInfo.setEamIDBusiTypemultiselectNames(names);
	}
	private void dealEamIDDeptID(BEAMBaseInfo baseInfo) {
		String addIds = baseInfo.getEamIDDeptIDAddIds();
		String delIds = baseInfo.getEamIDDeptIDDeleteIds();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("deptID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		if (addIds != null && addIds.length() > 0) {
			String[] ids = addIds.split(",");
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					BEAMBaseCheck tmp = new BEAMBaseCheck();
					tmp.setEamID(baseInfo);
					Department indirectObj = new Department();
					indirectObj.setId(Long.valueOf(item));
					tmp.setDeptID(indirectObj);
					tmp.setCid(baseInfo.getCid());
					baseCheckDao.save(tmp);
				}
			}
		}
	}

	private void setEamIDDeptID(BEAMBaseInfo baseInfo) {
		setEamIDDeptID(baseInfo, null);
	}

	private void setEamIDDeptID(BEAMBaseInfo baseInfo, String viewCode) {
		List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Order.asc("id"), Restrictions.eq("eamID", baseInfo), Restrictions.eq("valid", true));
		String ids = "";
		String names = "";
		for (BEAMBaseCheck item : tmpList) {
			if(item.getDeptID() != null) {
				if(ids.length() > 0) {
					ids += ",";
					names += ",";
				}
				ids += item.getDeptID().getId();
				String displayField = "name";
				if(viewCode != null) {
					switch (viewCode) {
					}
				}
				names += ReflectUtils.getPropertyValue(item.getDeptID(), displayField);
			}
		}
		baseInfo.setBaseCheckList(tmpList);
		baseInfo.setEamIDDeptIDmultiselectIDs(ids);
		baseInfo.setEamIDDeptIDmultiselectNames(names);
	}
	private void dealEamIDJwxRuleHead(BEAMBaseInfo baseInfo) {
		String addIds = baseInfo.getEamIDJwxRuleHeadAddIds();
		String delIds = baseInfo.getEamIDJwxRuleHeadDeleteIds();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("jwxRuleHead.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		if (addIds != null && addIds.length() > 0) {
			String[] ids = addIds.split(",");
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					BEAMBaseCheck tmp = new BEAMBaseCheck();
					tmp.setEamID(baseInfo);
					BEAMJwxRuleHead indirectObj = new BEAMJwxRuleHead();
					indirectObj.setId(Long.valueOf(item));
					tmp.setJwxRuleHead(indirectObj);
					tmp.setCid(baseInfo.getCid());
					baseCheckDao.save(tmp);
				}
			}
		}
	}

	private void setEamIDJwxRuleHead(BEAMBaseInfo baseInfo) {
		setEamIDJwxRuleHead(baseInfo, null);
	}

	private void setEamIDJwxRuleHead(BEAMBaseInfo baseInfo, String viewCode) {
		List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Order.asc("id"), Restrictions.eq("eamID", baseInfo), Restrictions.eq("valid", true));
		String ids = "";
		String names = "";
		for (BEAMBaseCheck item : tmpList) {
			if(item.getJwxRuleHead() != null) {
				if(ids.length() > 0) {
					ids += ",";
					names += ",";
				}
				ids += item.getJwxRuleHead().getId();
				String displayField = "id";
				if(viewCode != null) {
					switch (viewCode) {
					}
				}
				names += ReflectUtils.getPropertyValue(item.getJwxRuleHead(), displayField);
			}
		}
		baseInfo.setBaseCheckList(tmpList);
		baseInfo.setEamIDJwxRuleHeadmultiselectIDs(ids);
		baseInfo.setEamIDJwxRuleHeadmultiselectNames(names);
	}
	private void dealEamIDPositionID(BEAMBaseInfo baseInfo) {
		String addIds = baseInfo.getEamIDPositionIDAddIds();
		String delIds = baseInfo.getEamIDPositionIDDeleteIds();
		// 删除
		if (delIds != null && delIds.length() > 0) {
			String[] ids = delIds.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					idList.add(Long.valueOf(item));
				}
			}
			if (!idList.isEmpty()) {
				List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo), Restrictions.in("positionID.id", idList), Restrictions.eq("valid", true));
				for (BEAMBaseCheck item : tmpList) {
					baseCheckDao.deletePhysical(item);
				}
			}
		}
		if (addIds != null && addIds.length() > 0) {
			String[] ids = addIds.split(",");
			for (String item : ids) {
				if (item != null && item.length() > 0) {
					BEAMBaseCheck tmp = new BEAMBaseCheck();
					tmp.setEamID(baseInfo);
					Position indirectObj = new Position();
					indirectObj.setId(Long.valueOf(item));
					tmp.setPositionID(indirectObj);
					tmp.setCid(baseInfo.getCid());
					baseCheckDao.save(tmp);
				}
			}
		}
	}

	private void setEamIDPositionID(BEAMBaseInfo baseInfo) {
		setEamIDPositionID(baseInfo, null);
	}

	private void setEamIDPositionID(BEAMBaseInfo baseInfo, String viewCode) {
		List<BEAMBaseCheck> tmpList = baseCheckDao.findByCriteria(Order.asc("id"), Restrictions.eq("eamID", baseInfo), Restrictions.eq("valid", true));
		String ids = "";
		String names = "";
		for (BEAMBaseCheck item : tmpList) {
			if(item.getPositionID() != null) {
				if(ids.length() > 0) {
					ids += ",";
					names += ",";
				}
				ids += item.getPositionID().getId();
				String displayField = "name";
				if(viewCode != null) {
					switch (viewCode) {
					}
				}
				names += ReflectUtils.getPropertyValue(item.getPositionID(), displayField);
			}
		}
		baseInfo.setBaseCheckList(tmpList);
		baseInfo.setEamIDPositionIDmultiselectIDs(ids);
		baseInfo.setEamIDPositionIDmultiselectNames(names);
	}
	// ================ 生成多选控件数据保存方法 end ====================

	protected String getEntityCode(){return BEAMBaseInfo.ENTITY_CODE;}
	protected String getModuleCode(){return BEAMBaseInfo.MODULE_CODE;}
	protected String getModelCode(){return BEAMBaseInfo.MODEL_CODE;}
	
	
	/**
	 * 检查模型字段的唯一性约束
	 * @param baseInfo 模型实例
	 */
	public void checkUniqueConstraint(BEAMBaseInfo baseInfo){
		if(null == baseInfo.getCode() || StringUtils.isEmpty(baseInfo.getCode().toString())){
			return;
		}
		if(baseInfo.getId()==null ){
			List<BEAMBaseInfo> baseInfos = baseInfoDao.findByCriteria(Restrictions.eq("code", baseInfo.getCode()));
			if(baseInfos!=null && !baseInfos.isEmpty()){
				throw new BAPException(BAPException.Code.UNIQUE_FIELD,InternationalResource.get(
						"BEAM.propertydisplayName.randon1461811874200", getCurrentUser().getLanguage()), baseInfo.getCode());
			}
		} else {
			List<BEAMBaseInfo> baseInfos = baseInfoDao.findByCriteria(Restrictions.ne("id", baseInfo.getId()), Restrictions.eq("code", baseInfo.getCode()));
			if(baseInfos!=null && !baseInfos.isEmpty()){
				throw new BAPException(BAPException.Code.UNIQUE_FIELD,InternationalResource.get(
						"BEAM.propertydisplayName.randon1461811874200", getCurrentUser().getLanguage()), baseInfo.getCode());
			}
		}
	}
	
	
	@Override
	public List<BEAMBaseInfo> getBaseInfos(String sql, List<Object> params) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BEAMBaseInfo.class);
		if(params!=null){
			List<Type> types = new ArrayList<Type>();
			for(Object obj : params){
				types.add(TypeFactory.basic(obj.getClass().getName()));
			}
			detachedCriteria.add(Restrictions.sqlRestriction(sql, params.toArray(new Object[]{}),types.toArray(new Type[]{})));
		}else{
			return baseInfoDao.findByHql("from " + BEAMBaseInfo.JPA_NAME + " " + ((sql==null || sql.isEmpty()) ? "" : "where " + sql)) ;
		}
		return baseInfoDao.findByCriteria(detachedCriteria);
	}
	
	
	
	@Override
	public List<BEAMBaseInfo> findBaseInfosBySql(String sql, List<Object> params){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BEAMBaseInfo.class);
		if(params!=null && params.size()>0){
			List<Type> types = new ArrayList<Type>();
			for(Object obj : params){
				types.add(TypeFactory.basic(obj.getClass().getName()));
			}
			detachedCriteria.add(Restrictions.sqlRestriction(sql, params.toArray(new Object[]{}),types.toArray(new Type[]{})));
			
		}else{
			detachedCriteria.add(Restrictions.sqlRestriction(sql));
		}
		
		return baseInfoDao.findByCriteria(detachedCriteria);
	}
	@Override
	public List<BEAMBaseInfo> findBaseInfosByHql(String hql, Object... objects){
		
		return   baseInfoDao.findByHql(hql, objects);
	}
	
	@Override
	public BEAMBaseInfo loadBaseInfoByBussinessKey(BEAMBaseInfo baseInfo){
		return loadBaseInfoByBussinessKey(baseInfo.getCode());
	}
	
	@Override
	public Page<BEAMBaseInfo> getBaseInfos(Page<BEAMBaseInfo> page, String sql, List<Object> params, String sort) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BEAMBaseInfo.class);
		if(sort!=null && sort.length()>0){
			String[] sorts = sort.split("\\|");
			if(sorts!=null && sorts.length>0){
				for(String condition : sorts){
					if(condition!=null && condition.length()>0 && condition.contains(",")){
						String[] conditions = condition.split(",");
						if("asc".equals(conditions[1])){
							detachedCriteria.addOrder(Order.asc(conditions[0]));
						}else if("desc".equals(conditions[1])){
							detachedCriteria.addOrder(Order.desc(conditions[0]));
						}
					}
				}
			}
		}
		if(params!=null){
			List<Type> types = new ArrayList<Type>();
			for(Object obj : params){
				types.add(TypeFactory.basic(obj.getClass().getName()));
			}
			detachedCriteria.add(Restrictions.sqlRestriction(sql, params.toArray(new Object[]{}),types.toArray(new Type[]{})));
		}else{
			if(sql!=null){
				page = baseInfoDao.findByPage(page, sql);
			}else{
				page = baseInfoDao.findAllByPage(page);
			}
			return page;
		}
		page = baseInfoDao.findByPage(page, detachedCriteria);
		return page;
	}
	//==============DataGrid多选控件使用 start================
	
	
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BEAMBaseInfo loadBaseInfoByBussinessKey(Serializable bussinessKey){
		return baseInfoDao.findEntityByCriteria(Restrictions.eq("code", bussinessKey),Restrictions.eq("valid", true));
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<BEAMBaseInfo> findByProperty(String propertyName, Object object){
		return baseInfoDao.findByProperty(propertyName, object);
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BEAMBaseInfo findEntityByProperty(String propertyName, Object object){
		return baseInfoDao.findEntityByProperty(propertyName, object);
	}
	
	
	
	@Override
	@Transactional
	public void deleteByBussinessKeys(String bussinessKeys){
		if(null != bussinessKeys && bussinessKeys.trim().length() > 0){
			List<String> bussinessKeyList = Arrays.asList(bussinessKeys.split(","));
			String sql = "update " + BEAMBaseInfo.JPA_NAME + " set valid=0 where code in (:businessKeys)";
			Query query = baseInfoDao.createQuery(sql);
			query.setParameterList("businessKeys", bussinessKeyList);
			query.executeUpdate();
		}
	}
	
	@Override
	public String findValidateDatagrids(Map<String,Class> dgClassMap){
		String bap_validate_datagrids="";
		List<View> dgviews = viewServiceFoundation.findViewsByAssModelCode("BEAM_1.0.0_baseInfo_BaseInfo","runtime");
		try {
			for(View dgv:dgviews){
				if (null == dgv.getIsShadow() || !dgv.getIsShadow()) {
					List<DataGrid> ldg=viewServiceFoundation.getDataGrids(dgv.getCode());
					for(DataGrid dg:ldg){
						bap_validate_datagrids+=dg.getName()+",";
						dgClassMap.put(dg.getName(),Class.forName("com.supcon.orchid.BEAM.entities."+dg.getTargetModel().getJpaName()));
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
		}
		return bap_validate_datagrids;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String findValidateDatagrids(Map<String,Class> dgClassMap,String viewCode){
		String bap_validate_datagrids="";
		List<DataGrid> dgs = viewServiceFoundation.getDataGrids(viewCode, "runtime");
		try {
			for(DataGrid dg:dgs){
				bap_validate_datagrids+=dg.getName()+",";
				dgClassMap.put(dg.getName(),Class.forName("com.supcon.orchid.BEAM.entities."+dg.getTargetModel().getJpaName()));
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
		}
		return bap_validate_datagrids;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String findMainViewPath(){
		String mainViewPath="";
		List<View> views = viewServiceFoundation.findViewsByEntityCode("BEAM_1.0.0_baseInfo", ViewType.VIEW);
		for(View v:views){
			if(v.getMainView()){
				mainViewPath=v.getUrl();
			}
		}
		return mainViewPath;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<DataGrid> findDatagrids(){
		List<DataGrid> ldg=new ArrayList<DataGrid>();
		List<View> dgviews = viewServiceFoundation.findViewsByAssModelCode("BEAM_1.0.0_baseInfo_BaseInfo","runtime");
		for(View dgv:dgviews){
			if (null == dgv.getIsShadow() || !dgv.getIsShadow()) {
				ldg.addAll(dgv.getDataGrids());
			}
		}
		return ldg;
	}
	
	public void dealDatagridsSave(BEAMBaseInfo baseInfo,String viewCode,Map<String,String> dgLists,Map<String,String> dgDeleteIDs,Map<String,Object> assFileUploads){
		if(dgLists==null&&dgDeleteIDs==null){
			return;
		}
		List<DataGrid> dgs=viewServiceFoundation.getDataGrids(viewCode, "runtime");
		if(dgs!=null&&dgs.size()>0){
			baseInfoDao.flush();
			Class _reflect_thisclass=this.getClass();
			try {
				for(DataGrid dg:dgs){
					if(null != dg.getDataGridType() && dg.getDataGridType() == 1){
						continue;
					}
					Class _reflect_dgclass=Class.forName("com.supcon.orchid.BEAM.entities."+dg.getTargetModel().getJpaName());
					java.lang.reflect.Field _reflect_serviceField = _reflect_thisclass.getDeclaredField(fLTL(dg.getTargetModel().getModelName())+"Service");
					Class _reflect_serviceType=_reflect_serviceField.getType();
					Object _reflect_serviceObj=_reflect_serviceField.get(this);
					Model _targetModel=dg.getTargetModel();
					List<Long> _needDealIds = new ArrayList<>();
					Map<String, Long> _dgCodeMap = new HashMap<String, Long>();
					boolean isTree=(_targetModel.getDataType()==2);
					List<String> _attachKey =new ArrayList<String>();
					Map _confMap=dg.getConfigMap();
					Map _layoutMap = (Map) _confMap.get("layout");
					List<Map> ls = (List<Map>) _layoutMap.get("sections");
					for(Map lm:ls){
						if(lm.get("regionType").equals("DATAGRID")){
							List<Map> lc = (List<Map>) lm.get("cells");
							for(Map lk:lc){
								if("PROPERTYATTACHMENT".equals(lk.get("columnType"))&&lk.get("key")!=null){
									_attachKey.add(lk.get("key").toString());
								}
							}
						}
					}
					boolean hasAttach=(_attachKey.size()>0);
					if(null == assFileUploads){
						assFileUploads = new HashMap<String,Object>();
					}
					if(hasAttach&&assFileUploads.get("staffId")==null){	
						assFileUploads.put("staffId", ((OrchidAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getStaff().getId());
					}
					Map<String,Object> dgAttachMap = (Map<String, Object>) assFileUploads.get(dg.getName());
					if(hasAttach){
						if(null == dgAttachMap){
							dgAttachMap = new HashMap<String,Object>();
						}
						for(String key:_attachKey){
							dgAttachMap.put(key+"_propertyCode",_reflect_dgclass.getDeclaredField(key.toUpperCase()+"_PROPERTY_CODE").get(null));
						}
						dgAttachMap.put("type", _reflect_dgclass.getDeclaredField("DOC_TYPE").get(null));
					}
					if(dgDeleteIDs!=null&&dgDeleteIDs.get(dg.getName())!=null){
						ArrayList<Long> param=new ArrayList<Long>();
						String[] arrs=dgDeleteIDs.get(dg.getName()).split(",");
						for(int i=0;i<arrs.length;i++){
							param.add(Long.valueOf(arrs[i]));
						}
						if(hasAttach){
							dgAttachMap.put("deleteIds",param);
						}
						_reflect_serviceType.getDeclaredMethod("delete"+_targetModel.getModelName(),List.class,String.class).invoke(_reflect_serviceObj,param,"noEvent");
					}
					if(dgLists!=null&&dgLists.get(dg.getName())!=null){
						if(isTree){
							_needDealIds.clear();
							_dgCodeMap.clear();
						}
						List dglist=(List)com.supcon.orchid.ec.utils.JSONUtil.generateObjectFromJson(dgLists.get(dg.getName()).replaceAll("\r\n", "\\\\r\\\\n"), _reflect_dgclass,baseInfoDao.getSessionFactory());
						java.lang.reflect.Method _reflect_setMethod=_reflect_dgclass.getMethod("set"+fLTU(dg.getOrgProperty().getName()), BEAMBaseInfo.class);
						java.lang.reflect.Method _reflect_saveMethod=_reflect_serviceType.getMethod("save"+_targetModel.getModelName(),_reflect_dgclass,Map.class,Map.class,Map.class,String.class,String.class,boolean[].class);
						for(Object odg:dglist){
							_reflect_setMethod.invoke(odg, baseInfo);
							Map<String,Object> paramMap = new HashMap<String,Object>();
							if(hasAttach){
								for(String key:_attachKey){
									String fileAddPath=(String)_reflect_dgclass.getDeclaredMethod("get"+fLTU(key)+"FileAddPaths", null).invoke(odg, null);
									if(fileAddPath!=null&&fileAddPath.length()>0){
										paramMap.put(key+"_fileAddPaths",fileAddPath);
									}
									String fileDeleteIds=(String)_reflect_dgclass.getDeclaredMethod("get"+fLTU(key)+"FileDeleteIds", null).invoke(odg, null);
									if(fileDeleteIds!=null&&fileDeleteIds.length()>0){
										paramMap.put(key+"_fileDeleteIds",fileDeleteIds);
									}
								}
							}
							if(isTree){
								String ppCode = ((com.supcon.orchid.tree.AbstractCidTreeNode)odg).getVirtualParentCode();
								if(null != ppCode && ppCode.indexOf("x") > -1) {
									if(_dgCodeMap.containsKey(ppCode)) {
										((com.supcon.orchid.tree.AbstractCidTreeNode)odg).setParentId(_dgCodeMap.get(ppCode));
									}
								} else if(null != ppCode && !ppCode.isEmpty()) {
									((com.supcon.orchid.tree.AbstractCidTreeNode)odg).setParentId(Long.parseLong(ppCode));
								}
							}
							_reflect_saveMethod.invoke(_reflect_serviceObj, odg,null,null,null,viewCode,"noEvent",new boolean[]{});
							if(isTree){
								_needDealIds.add(((com.supcon.orchid.tree.AbstractCidTreeNode)odg).getId());
								String pCode = ((com.supcon.orchid.tree.AbstractCidTreeNode)odg).getVirtualCode();
								if(!_dgCodeMap.containsKey(pCode)) {
									_dgCodeMap.put(pCode, ((com.supcon.orchid.tree.AbstractCidTreeNode)odg).getId());
								}
							}
							if(!paramMap.isEmpty()){
								paramMap.put("linkId",((com.supcon.orchid.orm.entities.IdEntity)odg).getId());
								paramMap.put("mainModelId",baseInfo.getId());	
								dgAttachMap.put(((com.supcon.orchid.orm.entities.IdEntity)odg).getId().toString(), paramMap);
							}
						}
						if(isTree){
							_reflect_serviceType.getMethod("deal"+_targetModel.getModelName()+"Leaf",List.class).invoke(_reflect_serviceObj, _needDealIds);
						}
					}
					if(hasAttach){
						if(null != dgAttachMap && !dgAttachMap.isEmpty()){
							assFileUploads.put(dg.getName(),dgAttachMap);
						}
					}
				}
			}catch (Exception e) {
				log.error(e.getMessage(),e);
				if(e instanceof InvocationTargetException){
					InvocationTargetException iteException=(InvocationTargetException)e;
					if(iteException.getTargetException()!=null){
						Throwable throwable=iteException.getTargetException();
						if(throwable instanceof BAPException){
					     	 throw (BAPException)throwable;
					    }else{
							throw new BAPException(throwable.getMessage()!=null?throwable.getMessage():InternationalResource.get("foundation.error.message", getCurrentLanguage()));
						}
					}
				}
				throw new BAPException(InternationalResource.get("foundation.error.message", getCurrentLanguage()));
			}
		}
	} 

	
		@Override
	@Transactional
	public List<Object> getBusinessKeyData(String businessKeyName){
		List<Object> list = new ArrayList<Object>();
		if(null != businessKeyName){
			String sql = "select " + businessKeyName +" from "+ BEAMBaseInfo.TABLE_NAME +" where valid = 1";
			list = baseInfoDao.createNativeQuery(sql).list();
		}
		return list;
	}
	
	@Override
	@Transactional
	public List<Object> getBusinessKeyDataByBusinessKeyName(String businessKeyName){
		List<Object> list = new ArrayList<Object>();
		if(null != businessKeyName){
			String sql = "select " + businessKeyName +" from "+ BEAMBaseInfo.TABLE_NAME;
			list = baseInfoDao.createNativeQuery(sql).list();
		}
		return list;
	}
	
	@Override
	@Transactional
	public Map<Object, Object> getReplaceProperty(String propertyName,String businessKey){
		List<Object[]> list = new ArrayList<Object[]>();
		Map<Object, Object> m = new HashMap<Object, Object>();
		if(null != propertyName && null != businessKey){
			String sql = "select " + businessKey + "," + propertyName +" from "+ BEAMBaseInfo.TABLE_NAME +" group by " + propertyName + "," + businessKey;
			list = baseInfoDao.createNativeQuery(sql).list();
		}
		if (list != null && !list.isEmpty()) {
			for (Object[] obj : list) {
				if(null != obj[1]){
					m.put(obj[0], obj[1]);
				}
			}
		}
		return m;
	}
	
	@Autowired
	private BEAMBaseInfoDaoImpl baseInfoDaoImpl;
	
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="导入业务信息",operType="5")
	public Map<Object, Long> importBatchBaseInfo(final List<BEAMBaseInfo> insertObjs, final List<BEAMBaseInfo> updateObjs, 
				List<Map<String,String>> columnInfo, Map<String ,List<Map<String, Object>>> importNodeInfo, Map<String, Property> importPropInfo){
		return this.importBatchBaseInfo(insertObjs, updateObjs, columnInfo, importNodeInfo, importPropInfo,null);		
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="导入业务信息",operType="5")
	public Map<Object, Long> importBatchBaseInfo(final List<BEAMBaseInfo> insertObjs, final List<BEAMBaseInfo> updateObjs, 
				List<Map<String,String>> columnInfo, Map<String ,List<Map<String, Object>>> importNodeInfo, Map<String, Property> importPropInfo,SignatureLog signatureLog){
				
		int importCount = 0;
		if (null != insertObjs) {
			importCount += insertObjs.size();
		}
		if (null != updateObjs) {
			importCount += updateObjs.size();
		}
		AuditUtil.setAuditDes("导入" + InternationalResource.get("BEAM.modelname.randon1438842563083") +"信息" + importCount + "条");
		if(null != signatureLog && null == signatureLog.getOperateLogUuid()){
			if (null != AuditUtil.getCurrentAudit() && null != AuditUtil.getCurrentAudit().getOperationAudit()) {
				signatureLog.setOperateLogUuid((null == AuditUtil.getCurrentAudit().getOperationAudit().get_parentCode() || "-1".equals( AuditUtil
						.getCurrentAudit().getOperationAudit().get_parentCode())) ? AuditUtil.getCurrentAudit().getOperationAudit().getUuid() : AuditUtil
						.getCurrentAudit().getOperationAudit().get_parentCode());
			}
		}
		Map<Long, BEAMBaseInfo> oldUpdateObjsMap = new HashMap<Long, BEAMBaseInfo>(); 
		List<Long> updateObjIdsList = new ArrayList<Long>();
		for(int updateIndex = 0; updateIndex < updateObjs.size(); updateIndex++)  {
			BEAMBaseInfo bizObj = updateObjs.get(updateIndex);
			updateObjIdsList.add(bizObj.getId());
		}
		if (updateObjIdsList.size() > 0) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BEAMBaseInfo.class);
			if(updateObjIdsList.size()>1000){
				List<Long> updateObjIdsListBefore = updateObjIdsList.subList(0, 999);
				List<Long> updateObjIdsListAfter = updateObjIdsList.subList(999, updateObjIdsList.size());		
				detachedCriteria.add(Restrictions.or(Restrictions.in("id", updateObjIdsListBefore),Restrictions.in("id", updateObjIdsListAfter)));
			}else{
				detachedCriteria.add(Restrictions.in("id", updateObjIdsList));
			}
			List<BEAMBaseInfo> bizObjList = baseInfoDao.findByCriteria(detachedCriteria);
			if (null != bizObjList) {
				for (int mIndex = 0; mIndex < bizObjList.size(); mIndex++) {
					BEAMBaseInfo bizObj = bizObjList.get(mIndex);
					if (null != bizObj && null != bizObj.getId()) {
						oldUpdateObjsMap.put(bizObj.getId(), bizObj);
					}
				}
			}
		}		
				
		final List<Map<String,String>> columnMaps = new ArrayList<Map<String,String>>();
		List<String> columnName = new ArrayList<String>();
		final List<String> associatedModelNames = new ArrayList<String>();
		final List<String> associatedModelTypes = new ArrayList<String>();
		for(int i = 0 ; i<columnInfo.size(); i++){//封装excel中列名对应的模型中的字段，数据库中的字段，字段类型
			if(!columnName.contains(columnInfo.get(i).get("name")) && !columnInfo.get(i).get("name").equals("id")){
				Map<String,String> columnMap = new HashMap<String, String>();
				columnMap.put("name", columnInfo.get(i).get("name"));
				String name = columnInfo.get(i).get("name");
				if(name.equals("deleteStaff") || name.equals("createStaff") || name.equals("modifyStaff") || name.equals("effectStaff") || name.equals("ownerDepartment") || 
						name.equals("ownerPosition") || name.equals("createPosition") || name.equals("createDepartment") || name.equals("ownerStaff")){
					name = name + "Id";
				}
				columnMap.put("dbname", columnInfo.get(i).get("dbname"));
				columnMap.put("type", columnInfo.get(i).get("type"));
				columnMap.put("isCustom", columnInfo.get(i).get("isCustom"));
				columnMap.put("multable", columnInfo.get(i).get("multable"));
				columnMaps.add(columnMap);
				columnName.add(columnInfo.get(i).get("name"));
				associatedModelNames.add(columnInfo.get(i).get("associatedModelName"));
				associatedModelTypes.add(columnInfo.get(i).get("associatedModelType"));
			}
		}
		
		final List<BEAMBaseInfo> nmeObjs = new ArrayList<BEAMBaseInfo>();
		
		Map<Object, Long> m = new HashMap<Object, Long>();
		
		final String dbId = getDbIdName();
		final List<Long> insertIds = new ArrayList<Long>();
		int expLength=insertObjs.size();
		long id=autoGeneratorID.getNextId(BEAMBaseInfo.TABLE_NAME,expLength,dbId);//id表只操作一次，实际id通过++来增长
		for(BEAMBaseInfo baseInfo:insertObjs)  {
			//id = autoGeneratorID.getNextId(BEAMBaseInfo.TABLE_NAME,1,dbId);
			if(baseInfo.getCode() != null && !baseInfo.getCode().equals("") ){
				m.put(baseInfo.getCode(), id);
			}else{
				m.put(id, id);
			}
			insertIds.add(id);
			baseInfo.setId(id);
			id++;
		}
		
		
		for(Map<String,String> map : columnMaps){
			if(map.get("type").equals("SYSTEMCODE")){
				String sql = "select senior_system_code,multable from runtime_property where code = ?";
				Object[] result = (Object[]) baseInfoDao.createNativeQuery(sql, "BEAM_1.0.0_baseInfo_BaseInfo"+"_"+map.get("name")).uniqueResult();
				map.put("isSenior", result[0].toString());
				//map.put("multable", result[1].toString());
			}
		}
		
		baseInfoDaoImpl.getSession().doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement updateps = null;
				PreparedStatement insertps = null;
				User currentUser=(User)getCurrentUser();
				Staff currentStaff=currentUser.getStaff();
				
				if(updateObjs != null && updateObjs.size()>0){
					List<String> updateMethods = new ArrayList<String>();
					String updateSql = "UPDATE " + BEAMBaseInfo.TABLE_NAME + " SET ";
					for(int i = 0;i<columnMaps.size();i++){
						String mName = columnMaps.get(i).get("name");
						String methodName = "get"+ mName.replaceFirst(mName.substring(0, 1), mName.substring(0, 1).toUpperCase());
						updateMethods.add(methodName);
						updateSql += columnMaps.get(i).get("dbname")+"=?,";
					}
					updateSql = updateSql.substring(0,updateSql.length()-1);
					updateSql += " where " + dbId + " =?";
					
					updateps = conn.prepareStatement(updateSql);
					for(int updateIndex =0;updateIndex<updateObjs.size();updateIndex++){
						BEAMBaseInfo baseInfo = updateObjs.get(updateIndex);
						Long id = baseInfo.getId();
						if(null != id){
							updateps.setLong(columnMaps.size()+1,id);
							for(int i=0;i<columnMaps.size();i++){
								Method method = null;
								try {
									method = baseInfo.getClass().getMethod(updateMethods.get(i));
								} catch (NoSuchMethodException | SecurityException e) {
									log.error(e.getMessage(),e);
								}
								Object obj = null;
								if(null != method){
									try {
										obj = method.invoke(baseInfo);
									} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
										log.error(e.getMessage(),e);
									}
								}
								if(null != obj){
									String type = columnMaps.get(i).get("type");
									if (type.equals("TEXT") || type.equals("PASSWORD") || type.equals("LONGTEXT") || type.equals("BAPCODE") || type.equals("SUMMARY")) {
										updateps.setString(i+1, obj.toString());
									} else if(type.equals("SYSTEMCODE")){
										if( null != columnMaps.get(i).get("isCustom") && columnMaps.get(i).get("isCustom").equals("true") ){
											updateps.setString(i+1, obj.toString());
										}else if( null != columnMaps.get(i).get("multable") && columnMaps.get(i).get("multable").equals("true") ){
											updateps.setString(i+1, obj.toString());
										}else if(null != columnMaps.get(i).get("isSenior") && columnMaps.get(i).get("isSenior").equals("0")){
											SystemCode sc = (SystemCode) obj;
											updateps.setString(i+1, sc.getId());
										}else{
											if(obj.toString().contains("SystemCode")){
												SystemCode sc = (SystemCode) obj;
												updateps.setString(i+1, sc.getId());
											}else{
												updateps.setString(i+1, obj.toString());
											}
										}
									} else if ("DATE".equals(type)) {
										java.sql.Date sqlDate=new java.sql.Date(((Date) obj).getTime());
										updateps.setDate(i+1, sqlDate);
									} else if ("DATETIME".equals(type)) {
										java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(((Date) obj).getTime());
										updateps.setTimestamp(i+1, sqlTimestamp);
									} else if ("INTEGER".equals(type)) {
										updateps.setInt(i+1, (Integer) obj);
									} else if ("LONG".equals(type)) {
										updateps.setLong(i+1, (Long) obj);
									} else if ("DECIMAL".equals(type) || "MONEY".equals(type)) {
										updateps.setBigDecimal(i+1, (BigDecimal) obj);
									} else if ("BOOLEAN".equals(type)) {
										updateps.setBoolean(i+1 , (Boolean) obj);
									} else if ("OBJECT".equals(type)) {
										if(null != columnMaps.get(i).get("isCustom") && columnMaps.get(i).get("isCustom").equals("true")){
											updateps.setLong(i+1, (Long) obj);
										}else{
											Method objmethod = null;
											try {
												String methodName = "get" + associatedModelNames.get(i).substring(0, 1).toUpperCase() +  associatedModelNames.get(i).substring(1, associatedModelNames.get(i).length());
												objmethod = obj.getClass().getMethod(methodName);
												if(associatedModelTypes.get(i).equals("LONG")){
													Long objId = null;
													objId = (Long) objmethod.invoke(obj);
													updateps.setObject(i+1, objId);
												}else if(associatedModelTypes.get(i).equals("TEXT")){
													String objCode = null;
													objCode = (String) objmethod.invoke(obj);
													updateps.setObject(i+1, objCode);
												}
											} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
												log.error(e.getMessage(),e);
											}	
											
										}							}
								}else{
									updateps.setString(i+1, null);
								}
							}
							nmeObjs.add(baseInfo);
							updateps.addBatch();
						}
					}	
				}
				
				if(insertObjs != null && insertObjs.size()>0){//新增
					List<String> insertMethods = new ArrayList<String>();
					String insertSql = "INSERT INTO " + BEAMBaseInfo.TABLE_NAME + " (";
					for(int i = 0;i<columnMaps.size();i++){
						String mName = columnMaps.get(i).get("name");
						String methodName = "get"+ mName.replaceFirst(mName.substring(0, 1), mName.substring(0, 1).toUpperCase());
						insertMethods.add(methodName);
						insertSql += columnMaps.get(i).get("dbname")+",";
					}
					insertSql += dbId + ",CID,CREATE_STAFF_ID,CREATE_TIME,EFFECTIVE_STATE) VALUES (";
					for(int i = 0;i<columnMaps.size();i++){
						insertSql += "?,";
					}
					insertSql += "?,?,?,?,?)";
					
//					String idSql = "select id from " + BEAMBaseInfo.TABLE_NAME + " order by id desc";
//					Query query = baseInfoDao.createNativeQuery(idSql).setMaxResults(1);
//					Long id = new Long(1000);
//					if(null != query.uniqueResult()){
//						id = Long.valueOf(query.uniqueResult().toString());
//					}
					insertps = conn.prepareStatement(insertSql);
					for(int insertIndex = 0; insertIndex<insertObjs.size(); insertIndex++)  {
						BEAMBaseInfo baseInfo = insertObjs.get(insertIndex);
						insertps.setLong(columnMaps.size() + 1, insertIds.get(insertIndex));
						insertps.setLong(columnMaps.size() + 2,getCurrentCompanyId());
						insertps.setLong(columnMaps.size() + 3,currentStaff.getId());
						java.util.Date date=new java.util.Date();
						insertps.setTimestamp(columnMaps.size() + 4,new java.sql.Timestamp(date.getTime()));
						insertps.setInt(columnMaps.size() + 5,0);
						for (int i = 0; i < columnMaps.size(); i++) {
							Method method = null;
							try {
								method = baseInfo.getClass().getMethod(
										insertMethods.get(i));
							} catch (NoSuchMethodException | SecurityException e) {
								log.error(e.getMessage(),e);
							}
							Object obj = null;
							if (null != method) {
								try {
									obj = method.invoke(baseInfo);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									log.error(e.getMessage(),e);
								}
							}
							if (null != obj) {
								String type = columnMaps.get(i).get("type");
								if (type.equals("TEXT") || type.equals("PASSWORD") || type.equals("LONGTEXT") || type.equals("BAPCODE") || type.equals("SUMMARY")) {
									insertps.setString(i+1, obj.toString());
								} else if(type.equals("SYSTEMCODE")){
									if( null != columnMaps.get(i).get("isCustom") && columnMaps.get(i).get("isCustom").equals("true") ){
										insertps.setString(i+1, obj.toString());
									}else if( null != columnMaps.get(i).get("multable") && columnMaps.get(i).get("multable").equals("true") ){
										insertps.setString(i+1, obj.toString());
									}else if(null != columnMaps.get(i).get("isSenior") && columnMaps.get(i).get("isSenior").equals("0")){
										SystemCode sc = (SystemCode) obj;
										insertps.setString(i+1, sc.getId());
									}else{
										if(obj.toString().contains("SystemCode")){
											SystemCode sc = (SystemCode) obj;
											insertps.setString(i+1, sc.getId());
										}else{
											insertps.setString(i+1, obj.toString());
										}
									}
								} else if ("DATE".equals(type)) {
									java.sql.Date sqlDate=new java.sql.Date(((Date) obj).getTime());
									insertps.setDate(i+1, sqlDate);
								} else if ("DATETIME".equals(type)) {
									java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(((Date) obj).getTime());
									insertps.setTimestamp(i+1, sqlTimestamp);
								} else if ("INTEGER".equals(type)) {
									insertps.setInt(i+1, (Integer) obj);
								} else if ("LONG".equals(type)) {
									insertps.setLong(i+1, (Long) obj);
								} else if ("DECIMAL".equals(type) || "MONEY".equals(type)) {
									insertps.setBigDecimal(i+1, (BigDecimal) obj);
								} else if ("BOOLEAN".equals(type)) {
									insertps.setBoolean(i+1 , (Boolean) obj);
								} else if ("OBJECT".equals(type)) {
									if(null != columnMaps.get(i).get("isCustom") && columnMaps.get(i).get("isCustom").equals("true")){
										insertps.setLong(i+1, (Long) obj);
									}else{
										Method objmethod = null;
										try {
											String methodName = "get" + associatedModelNames.get(i).substring(0, 1).toUpperCase() +  associatedModelNames.get(i).substring(1, associatedModelNames.get(i).length());
											objmethod = obj.getClass().getMethod(methodName);
											if(associatedModelTypes.get(i).equals("LONG")){
												Long objId = null;
												objId = (Long) objmethod.invoke(obj);
												insertps.setObject(i+1, objId);
											}else if(associatedModelTypes.get(i).equals("TEXT")){
												String objCode = null;
												objCode = (String) objmethod.invoke(obj);
												insertps.setObject(i+1, objCode);
											}
										} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
											log.error(e.getMessage(),e);
										}	
										
									}
								}
							} else {
								insertps.setString(i + 1, null);
							}
						}
						//baseInfo.setId(id);
						nmeObjs.add(baseInfo);
						insertps.addBatch();
					}		
				}
				
				PreparedStatement deletemneps = null;
				PreparedStatement savemneps = null;
				
				if(nmeObjs != null && nmeObjs.size()>0){//助记码操作
					Object mainEntity = nmeObjs.get(0);			
					Class<?> mainClazz = mainEntity.getClass();
					Class<?> mneCodeEntity = BEAMBaseInfoMneCode.class;

					String entityName = mainClazz.getSimpleName();
					//此段必须要用
					int idx = entityName.indexOf("_$$_javassist_");
					if(idx > 0) {
						mainClazz = Hibernate.getClass(mainEntity);
						entityName = entityName.substring(0, idx);
					}
					BAPMneField mneField = mneCodeEntity.getAnnotation(BAPMneField.class);
					if(mneField != null) {
						entityName = mneField.name();
					}
					entityName = Inflector.getInstance().columnize(com.supcon.orchid.utils.StringUtils.firstLetterToLower(entityName));
					
					final String dbEntityName = entityName;
					final Table mneCodeTable = mneCodeEntity.getAnnotation(Table.class);
					final Class<?> dbMainClazz = mainClazz;
					final Object dbMainEntity = mainEntity;
					
					//删除原有助记码
					String deleteSql = "DELETE FROM " + mneCodeTable.name() + " WHERE " + dbEntityName + " = ?";
					deletemneps = conn.prepareStatement(deleteSql);
					for(BEAMBaseInfo baseInfo:updateObjs)  {
						Long id = baseInfo.getId();
						deletemneps.setLong(1, id);
						deletemneps.addBatch();
					}	
					
					//生成并新增助记码
					String idSql = "select id from " + mneCodeTable.name() + " order by id desc";
					Query query = baseInfoDao.createNativeQuery(idSql).setMaxResults(1);
					Long id = new Long(1000);
					if(null != query.uniqueResult()){
						id = Long.valueOf(query.uniqueResult().toString());
					}
					String insertMneSql = "INSERT INTO " + mneCodeTable.name() + "(ID, MNE_CODE, " + dbEntityName
							+ ", VERSION) VALUES (?,?,?,?)";
					savemneps = conn.prepareStatement(insertMneSql);
					for (java.lang.reflect.Field f : dbMainClazz.getDeclaredFields()) {
						if (f.isAnnotationPresent(BAPIsMneCode.class)) { // 被标注为是否用于助记码
							String fieldName = f.getName();
							String getName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
							Method getMethod = null;
							try {
								getMethod = dbMainClazz.getMethod(getName);
							} catch (NoSuchMethodException | SecurityException e) {
								log.error(e.getMessage(),e);
							}
							if (getMethod.getReturnType() == String.class) {
								for(BEAMBaseInfo baseInfo:nmeObjs)  {
									String value = null;
									try {
										value = (String) getMethod.invoke(baseInfo);
									} catch (IllegalAccessException
											| IllegalArgumentException
											| InvocationTargetException e) {
										// TODO Auto-generated catch block
										log.error(e.getMessage(),e);
									}
									
									if (null != value) {
										Set<String> mneCodeSet = new HashSet<String>();
										List<String> mneCodeList = MneCodeGenterate.mneCodeTupleGenerate(value.length()>10?value.substring(0, 10):value);
										if (null != mneCodeList && !mneCodeList.isEmpty()) {
											mneCodeSet.addAll(mneCodeList);
										}
										mneCodeSet.add(value.toLowerCase());
										for (String mneCode : mneCodeSet) {
											id = autoGeneratorID.getNextId(mneCodeTable.name(),1,"ID");
											savemneps.setLong(1, id);
											savemneps.setString(2, mneCode);
											savemneps.setLong(3, baseInfo.getId());
											savemneps.setInt(4, 0);
											savemneps.addBatch();
										}
									}
								}	
							}
						}
					}	
				}
				
				if(null != deletemneps){
					deletemneps.executeBatch();
					deletemneps.close();	
				}
				if(null != savemneps){
					savemneps.executeBatch();
					savemneps.close();
				}
				
				if(null != insertps){
					insertps.executeBatch();
					insertps.close();
				}
				if(null != updateps){
					updateps.executeBatch();
					updateps.close();
				}
				
			}
		});	
		
		baseInfoDaoImpl.getSession().flush();
		baseInfoDaoImpl.getSession().clear();
		
		
		//记录导入数据日志（区分新增和修改）
		
		for(int insertIndex = 0; insertIndex < insertObjs.size(); insertIndex++)  {//新增
			BEAMBaseInfo bizObj = insertObjs.get(insertIndex);
			baseInfoImportService.saveImportDataLog(bizObj, null, "code", "name");
		}
		for(int updateIndex = 0; updateIndex < updateObjs.size(); updateIndex++)  {//修改
			BEAMBaseInfo bizObj = updateObjs.get(updateIndex);
			BEAMBaseInfo oldBizObj = oldUpdateObjsMap.get(bizObj.getId());
			baseInfoImportService.saveImportDataLog(bizObj, oldBizObj, "code", "name");
		}
		
		return m;
	}
	
	
	
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String getViewName(String modelCode){
		String sql = "select NAME from runtime_view where type='LIST' and ass_model_code = ?";
		String viewName = baseInfoDao.createNativeQuery(sql, modelCode).setMaxResults(1).uniqueResult()!=null?
				baseInfoDao.createNativeQuery(sql, modelCode).setMaxResults(1).uniqueResult().toString():"";
		return viewName;
	}
	
	
	
	
	private String getDbIdName(){
		String sql = "select column_name from runtime_property where model_code = ? and name = 'id'";
		String dbIDName = baseInfoDao.createNativeQuery(sql, "BEAM_1.0.0_baseInfo_BaseInfo").uniqueResult().toString();
		return dbIDName;
	}
	
	@Override
	public List<String> getSystemCodeFullPathNameByEntityCode(String entityCode) {
		String sql = "select value from base_systemcode where entity_code = ?";
		List<String> strs = baseInfoDao.createNativeQuery(sql, entityCode).list();
		return strs;
	}
	
	@Override
	public List<String> getRunningCustomProperties(String entityCode){
		List<String> list = null;
		String sql = "select property_code from BASE_CP_MODEL_MAPPING where model_code = ? and enable_custom = 1";
		list = baseInfoDao.createNativeQuery(sql, entityCode).list();
		return list;
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String getAssProperty(String propertyCode){
		String assProperty = null;
		String sql = "select associated_property_code from runtime_property where code = ?";
		assProperty = baseInfoDao.createNativeQuery(sql,propertyCode).uniqueResult()!=null?baseInfoDao.createNativeQuery(sql,propertyCode).uniqueResult().toString():"";
		return assProperty;
	}		
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String getPropertyModelCode(String propertyCode){
		String sql = "select model_code from runtime_property where code = ?";
		String modelCode =  baseInfoDao.createNativeQuery(sql,propertyCode).uniqueResult().toString();
		return modelCode;
	}
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public  Object generateObjectFromJson(String jsonStr, Class clazz){
		return com.supcon.orchid.ec.utils.JSONUtil.generateObjectFromJson(jsonStr,clazz,baseInfoDao.getSessionFactory());
	}
	
	/**
	 * 以下为兼容视图热部署之前代码的方法
	 */
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551623719Page(Page<BEAMDealInfoTable> dg1461551623719Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDealInfoTable.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"dealInfoTable\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		dealInfoTableDao.findByPage(dg1461551623719Page, criteria);
		dealInfoTableService.findByNormalDataGridBaseInfo(dg1461551623719Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551623828Page(Page<BEAMBaseCheck> dg1461551623828Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseCheck.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseCheck\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseCheckDao.findByPage(dg1461551623828Page, criteria);
		baseCheckService.findByNormalDataGridBaseInfo(dg1461551623828Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551623906Page(Page<BEAMAttachPart> dg1461551623906Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMAttachPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"attachPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		attachPartDao.findByPage(dg1461551623906Page, criteria);
		attachPartService.findByNormalDataGridBaseInfo(dg1461551623906Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551623969Page(Page<BEAMDocPart> dg1461551623969Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDocPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"docPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		docPartDao.findByPage(dg1461551623969Page, criteria);
		docPartService.findByNormalDataGridBaseInfo(dg1461551623969Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551624000Page(Page<BEAMSparePart> dg1461551624000Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMSparePart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"sparePart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		sparePartDao.findByPage(dg1461551624000Page, criteria);
		sparePartService.findByNormalDataGridBaseInfo(dg1461551624000Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461551857214Page(Page<BEAMBaseinfoParam> dg1461551857214Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseinfoParam.class);
		criteria.add(Restrictions.eq("eamId", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseinfoParam\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseinfoParamDao.findByPage(dg1461551857214Page, criteria);
		baseinfoParamService.findByNormalDataGridBaseInfo(dg1461551857214Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461552603723Page(Page<BEAMJWXItem> dg1461552603723Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1461552603723Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1461552603723Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461553165559Page(Page<BEAMJWXItem> dg1461553165559Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1461553165559Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1461553165559Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1461561957963Page(Page<BEAMJWXItem> dg1461561957963Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1461561957963Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1461561957963Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876472Page(Page<BEAMDealInfoTable> dg1536817876472Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDealInfoTable.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"dealInfoTable\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		dealInfoTableDao.findByPage(dg1536817876472Page, criteria);
		dealInfoTableService.findByNormalDataGridBaseInfo(dg1536817876472Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876535Page(Page<BEAMBaseCheck> dg1536817876535Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseCheck.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseCheck\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseCheckDao.findByPage(dg1536817876535Page, criteria);
		baseCheckService.findByNormalDataGridBaseInfo(dg1536817876535Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876628Page(Page<BEAMAttachPart> dg1536817876628Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMAttachPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"attachPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		attachPartDao.findByPage(dg1536817876628Page, criteria);
		attachPartService.findByNormalDataGridBaseInfo(dg1536817876628Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876675Page(Page<BEAMDocPart> dg1536817876675Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDocPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"docPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		docPartDao.findByPage(dg1536817876675Page, criteria);
		docPartService.findByNormalDataGridBaseInfo(dg1536817876675Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876738Page(Page<BEAMSparePart> dg1536817876738Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMSparePart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"sparePart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		sparePartDao.findByPage(dg1536817876738Page, criteria);
		sparePartService.findByNormalDataGridBaseInfo(dg1536817876738Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876831Page(Page<BEAMBaseinfoParam> dg1536817876831Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseinfoParam.class);
		criteria.add(Restrictions.eq("eamId", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseinfoParam\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseinfoParamDao.findByPage(dg1536817876831Page, criteria);
		baseinfoParamService.findByNormalDataGridBaseInfo(dg1536817876831Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817876894Page(Page<BEAMJWXItem> dg1536817876894Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1536817876894Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1536817876894Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817877050Page(Page<BEAMJWXItem> dg1536817877050Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1536817877050Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1536817877050Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1536817877159Page(Page<BEAMJWXItem> dg1536817877159Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1536817877159Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1536817877159Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261687888Page(Page<BEAMDealInfoTable> dg1462261687888Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDealInfoTable.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"dealInfoTable\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		dealInfoTableDao.findByPage(dg1462261687888Page, criteria);
		dealInfoTableService.findByNormalDataGridBaseInfo(dg1462261687888Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688029Page(Page<BEAMAttachPart> dg1462261688029Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMAttachPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"attachPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		attachPartDao.findByPage(dg1462261688029Page, criteria);
		attachPartService.findByNormalDataGridBaseInfo(dg1462261688029Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688091Page(Page<BEAMJWXItem> dg1462261688091Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1462261688091Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1462261688091Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688185Page(Page<BEAMJWXItem> dg1462261688185Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1462261688185Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1462261688185Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688278Page(Page<BEAMDocPart> dg1462261688278Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMDocPart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"docPart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		docPartDao.findByPage(dg1462261688278Page, criteria);
		docPartService.findByNormalDataGridBaseInfo(dg1462261688278Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688341Page(Page<BEAMJWXItem> dg1462261688341Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1462261688341Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1462261688341Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688606Page(Page<BEAMSparePart> dg1462261688606Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMSparePart.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"sparePart\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		sparePartDao.findByPage(dg1462261688606Page, criteria);
		sparePartService.findByNormalDataGridBaseInfo(dg1462261688606Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1462261688653Page(Page<BEAMBaseinfoParam> dg1462261688653Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseinfoParam.class);
		criteria.add(Restrictions.eq("eamId", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseinfoParam\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseinfoParamDao.findByPage(dg1462261688653Page, criteria);
		baseinfoParamService.findByNormalDataGridBaseInfo(dg1462261688653Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1530841303533Page(Page<BEAMBaseCheck> dg1530841303533Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMBaseCheck.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"baseCheck\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		baseCheckDao.findByPage(dg1530841303533Page, criteria);
		baseCheckService.findByNormalDataGridBaseInfo(dg1530841303533Page);
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void findDg1482737962957Page(Page<BEAMJWXItem> dg1482737962957Page,BEAMBaseInfo baseInfo, String condition, List<Object> params){
		DetachedCriteria criteria = DetachedCriteria.forClass(BEAMJWXItem.class);
		criteria.add(Restrictions.eq("eamID", baseInfo));
		criteria.add(Restrictions.eq("valid", true));
		if(!"".equals(condition) && condition.trim().length() > 0) {
			criteria.add(Restrictions.sqlRestriction(condition.replace("\"jWXItem\".", "{alias}."), params.toArray(), com.supcon.orchid.jdbc.DbUtils.getHibernateTypeByJavaType(params)));
		}
		jWXItemDao.findByPage(dg1482737962957Page, criteria);
		jWXItemService.findByNormalDataGridBaseInfo(dg1482737962957Page);
	}
	
	 
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="保存")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService){
		saveBaseInfo(baseInfo, dataGridService, null);
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="保存")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService, String viewCode){
		saveBaseInfo(baseInfo, dataGridService, viewCode, null);
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService, String viewCode, String eventTopic, boolean... isImport){
		this.saveBaseInfo(baseInfo, dataGridService, viewCode, eventTopic,null,isImport);
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void saveBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService, String viewCode, String eventTopic,SignatureLog signatureLog, boolean... isImport){
		boolean isNew = false;
		String entityCode = "BEAM_1.0.0_baseInfo";
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		String url = null;
		if(null != baseInfo.getId() && baseInfo.getId() > 0){
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("修改");
				AuditUtil.setAuditOperationType("2");
			}
			props.put("eventType", "modify");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/modify";
		}else{
			isNew = true;
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("新增");
				AuditUtil.setAuditOperationType("1");
			}
			props.put("eventType", "add");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/add";
		}
		List<Event> events = null;
		Boolean viewIsView = false;
		if(viewCode != null && !viewCode.trim().isEmpty()){
			View view = viewServiceFoundation.getView(viewCode);
			if(null != view) {
				viewIsView = (view.getType() == ViewType.VIEW);
			}
			events = viewServiceFoundation.getEventsByView(view);
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "beforeSave", events, baseInfo);
			}
		}
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo, viewIsView);

		if (viewIsView) {
			baseInfoDao.saveWithRevertVersion(baseInfo);
		} else {
			if(isNew)
					baseInfoDao.save(baseInfo);
				else
					baseInfoDao.merge(baseInfo);
		}
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		if (dataGridService != null) {
			baseInfoDao.flush();
			dataGridService.execute();
		}
		
		// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		
		// 根据配置规则生成编码
		try {
			generateBaseInfoCodes(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		// 根据配置规则生成摘要
		try {
			generateBaseInfoSummarys(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		
		afterSaveBaseInfo(baseInfo, viewIsView);


		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
		if(viewCode != null){
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "afterSave", events, baseInfo);
			}
		}
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		baseInfoDao.flush();
		baseInfoDao.clear();
		baseInfo = baseInfoDao.load(baseInfo.getId());
		
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		
		params.add(baseInfo);
		if(signatureLog != null) {
			Object businessKey=null;
			businessKey=baseInfo.getCode();
			if(businessKey != null) {
				signatureLog.setBusinessKey(businessKey.toString());
			}
			if(null != AuditUtil.getCurrentAudit() && null != AuditUtil.getCurrentAudit().getOperationAudit()){
				signatureLog.setOperateLogUuid(AuditUtil.getCurrentAudit().getOperationAudit().getUuid());
			}
			signatureLog.setTableId(baseInfo.getId());
			String msgId="moduleCode:BEAM_1.0.0#entityCode:BEAM_1.0.0_baseInfo#modelCode:BEAM_1.0.0_baseInfo_BaseInfo#timeStamp:"+String.valueOf(new Date().getTime());
			reliableMessageSenderService.sendQueue(msgId,signatureLog);
		}
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public Map<Object, Object> baseInfoDataGridImport(BEAMBaseInfo baseInfo, DataGridService dataGridService, String viewCode, String eventTopic,Property businessKey, boolean isImport){
		Map<Object, Object> map = new HashMap<Object, Object>();
		boolean isNew = false;
		String entityCode = "BEAM_1.0.0_baseInfo";
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		String url = null;
		if(baseInfo.getId() != null && baseInfo.getId() > 0){
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("修改");
				AuditUtil.setAuditOperationType("2");
			}
			props.put("eventType", "modify");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/modify";
		}else{
			isNew = true;
			if(StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD())){
				AuditUtil.setAuditDes("新增");
				AuditUtil.setAuditOperationType("1");
			}
			props.put("eventType", "add");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/add";
		}
		List<Event> events = null;
		Boolean viewIsView = false;
		if(viewCode != null && !viewCode.trim().isEmpty()){
			View view = viewServiceFoundation.getView(viewCode);
			if(null != view) {
				viewIsView = (view.getType() == ViewType.VIEW);
			}
			events = viewServiceFoundation.getEventsByView(view);
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "beforeSave", events, baseInfo);
			}
		}
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo, viewIsView);

		if (viewIsView) {
			baseInfoDao.saveWithRevertVersion(baseInfo);
		} else {
			if(isNew)
					baseInfoDao.save(baseInfo);
				else
					baseInfoDao.merge(baseInfo);
		}

		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		
		if (dataGridService != null) {
			baseInfoDao.flush();
			dataGridService.execute();
		}
		
		// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		
		// 根据配置规则生成编码
		try {
			generateBaseInfoCodes(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		// 根据配置规则生成摘要
		try {
			generateBaseInfoSummarys(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		
		afterSaveBaseInfo(baseInfo, viewIsView);


		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
		if(viewCode != null){
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "afterSave", events, baseInfo);
			}
		}
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		baseInfoDao.flush();
		baseInfoDao.clear();
		
		String virturalId = baseInfo.getVirtualId();
		
		baseInfo = baseInfoDao.load(baseInfo.getId());
		
		if(virturalId != null && virturalId.length() > 0){
			map.put(virturalId, baseInfo.getId());
		}else{
			String methodName = "get" + businessKey.getName().substring(0, 1).toUpperCase() + businessKey.getName().substring(1);
			Object bkValue = null;
			try {
				Method getBkValue = baseInfo.getClass().getMethod(methodName);
				bkValue = getBkValue.invoke(baseInfo);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
			map.put(bkValue, baseInfo.getId());
		}
		
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		
		params.add(baseInfo);
		return map;
	}
	
	 
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo")
	public void mergeBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService){
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo);
		baseInfoDao.merge(baseInfo);
		
		if(null != baseInfo && (StringUtils.isEmpty(AuditUtil.getColumnStringD()) || BEAMBaseInfo.MODEL_CODE.equals(AuditUtil.getColumnStringD()))){
			AuditUtil.setColumnStringB(null == baseInfo.getCode() ? "" : baseInfo.getCode().toString());
			AuditUtil.setColumnStringA(null == baseInfo.getName() ? "" : baseInfo.getName().toString());
			AuditUtil.setColumnStringC(baseInfo.getId().toString());
		}
		
		if (dataGridService != null) {
			dataGridService.execute();
		}

			// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		afterSaveBaseInfo(baseInfo);
		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
	}
	
	/**
	 * Excel导出 
	 */
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="excel导出",operType="11")
	public void excelExport(){}
	
	/**
	 * 页面打印 
	 */
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="页面打印",operType="4")
	public void print(int printType){
		if(printType == 0){
			AuditUtil.setAuditDes("页面打印");
		}else if(printType == 1){
			AuditUtil.setAuditDes("控件打印");
		}
	}
	
	@Override
	@AuditLog(entity="BEAM_1.0.0_baseInfo",model="BEAM_1.0.0_baseInfo_BaseInfo",desc="批量保存",operType="4")
	public void batchSaveBaseInfo(BEAMBaseInfo baseInfo, DataGridService dataGridService,View view,List<Event>  events, String eventTopic, boolean... isImport){
		String entityCode = "BEAM_1.0.0_baseInfo";
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("callType", "service");
		props.put("entityCode", "BEAM_1.0.0_baseInfo");
		String url = null;
		if(baseInfo.getId()!=null && baseInfo.getId()>0){
			props.put("eventType", "modify");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/modify";
		}else{
			props.put("eventType", "add");
			url = "com/supcon/orchid/entities/sync/bEAM_100_baseInfo/add";
		}
		Boolean viewIsView = false;
		if(view != null ){
			viewIsView = (view.getType() == ViewType.VIEW);
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "beforeSave", events, baseInfo);
			}
		}
		ReflectUtils.filterObjectIdIsNVL(baseInfo);
		beforeSaveBaseInfo(baseInfo, viewIsView);

		if (viewIsView) {
			baseInfoDao.saveWithRevertVersion(baseInfo);
		} else {
			baseInfoDao.save(baseInfo);
		}

		if (dataGridService != null) {
			baseInfoDao.flush();
			dataGridService.execute();
		}
		
		// 一对多情况处理
			this.dealEamIDStaffID(baseInfo);
			this.dealEamIDBusiType(baseInfo);
			this.dealEamIDDeptID(baseInfo);
			this.dealEamIDJwxRuleHead(baseInfo);
			this.dealEamIDPositionID(baseInfo);
		
		// 根据配置规则生成编码
		try {
			generateBaseInfoCodes(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		// 根据配置规则生成摘要
		try {
			generateBaseInfoSummarys(baseInfo, viewIsView);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BAPException(e.getMessage(), e);
		}
		
		afterSaveBaseInfo(baseInfo, viewIsView);


		baseInfoDao.flush();
		BAPEventPublisher.mneCodeGenerateEventPublisher("com/supcon/orchid/entities/mne/baseInfo/add", baseInfo,BEAMBaseInfoMneCode.class);
		if(view != null){
			if(events!=null && events.size()>0){
				executeGScript(entityCode, "afterSave", events, baseInfo);
			}
		}
		List<BEAMBaseInfo> params = new ArrayList<BEAMBaseInfo>();
		baseInfoDao.flush();
		baseInfoDao.clear();
		baseInfo = baseInfoDao.load(baseInfo.getId());
		
		List<BEAMBaseCheck> baseCheckList = baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo));
		baseInfo.setBaseCheckList(baseCheckList);
		List<BEAMBaseinfoParam> baseinfoParamList = baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo));
		baseInfo.setBaseinfoParamList(baseinfoParamList);
		
		params.add(baseInfo);
	}
	
	@Override
	public Page<BEAMBaseInfo> getByPage(Page<BEAMBaseInfo> page,DetachedCriteria detachedCriteria){
		return baseInfoDao.findByPage(page, detachedCriteria);
	}
	@Override
	public List<BEAMBaseCheck> getBaseCheckList(BEAMBaseInfo baseInfo){
		return baseCheckDao.findByCriteria(Restrictions.eq("eamID", baseInfo),Restrictions.eq("valid", true));
	}
	@Override
	public List<BEAMBaseinfoParam> getBaseinfoParamList(BEAMBaseInfo baseInfo){
		return baseinfoParamDao.findByCriteria(Restrictions.eq("eamId", baseInfo),Restrictions.eq("valid", true));
	}
	
	
	
	public String generateTableNo(){
		this.counter.setPattern(modelServiceFoundation.getEntity("BEAM_1.0.0_baseInfo").getPrefix()+"_{1,date,yyyyMMdd}_{0,number,000}");
		return this.counter.incrementAndGetString(new Date(),new Date());
	}
	private void beforeSaveBaseInfo(BEAMBaseInfo baseInfo, Object... objects){
		checkUniqueConstraint(baseInfo);
	/* CUSTOM CODE START(serviceimpl,beforeSave,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	
	private void afterSaveBaseInfo(BEAMBaseInfo baseInfo, Object... objects){
	/* CUSTOM CODE START(serviceimpl,afterSave,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

        BEAMDealInfoTable dealInfoTable = new BEAMDealInfoTable();
        Staff dealStaff=new Staff();
        /*
         * @author zhushizhang
         * @time 2017.3.9
         * @reason 添加修改人应该取当前登入人
         * ((User)getCurrentUser()).getStaff();
         */
        //dealStaff.setId(baseInfo.getModifyStaffId()==null?baseInfo.getCreateStaffId():baseInfo.getModifyStaffId());
        dealStaff.setId(((User)getCurrentUser()).getStaff().getId());
        dealInfoTable.setDealStaffID(dealStaff);
        dealInfoTable.setDealTime(new Date());
        dealInfoTable.setEamID(baseInfo);
        dealInfoTable.setContent(baseInfo.getContent());
        dealInfoTableDao.save(dealInfoTable);

        //获得业务类型的几个对象
        BEAMBusitype dian=new BEAMBusitype();
        BEAMBusitype run=new BEAMBusitype();
        BEAMBusitype wei=new BEAMBusitype();
        BEAMBusitype bao=new BEAMBusitype();
        BEAMBusitype jian=new BEAMBusitype();
        List<BEAMBusitype> bs=	busitypeService.findAllBusiTypes(false);
        for(BEAMBusitype b:bs){
            if(b!=null && b.getProperty()!=null && b.getProperty().getId()!=null ){

                String prop=b.getProperty().getId().toString();

                //点检
                if(prop!=null &&  prop.equals("BEAM010/02")){
                    dian=b;
                }
                //润滑
                if(prop!=null && prop.equals("BEAM010/03")){
                    run=b;
                }
                //维修
                if(prop!=null && prop.equals("BEAM010/05")){
                    wei=b;
                }
                //保养
                if(prop!=null && prop.equals("BEAM010/04")){
                    bao=b;
                }

            }
        }

        List<BEAMBusitype> bs2=	busitypeService.findAllBusiTypes(true);
        for(BEAMBusitype b:bs2){
            if(b!=null && b.getProperty()!=null && b.getProperty().getId()!=null ){
                String prop=b.getProperty().getId().toString();
                //检查
                if(prop!=null && prop.equals("BEAM010/01")){
                    jian=b;
                }

            }
        }

           /*
       //新增设备档案时，处理参照了模板，但是没有点击相关页签的情况
	  		if( (baseInfo.getVersion()==0)  &&  baseInfo.getTempentId()!=null ){
	  		 Integer temid=baseInfo.getTempentId();
	  		 String yeqian=	baseInfo.getPageProperty();

	  		 //如果没有点击技术参数页签
	  		 if(yeqian.indexOf("jishucanshu")<0){
	  				List<BEAMTemparamHead> parHeads=temparamHeadService.getTemparamHeads("templetID.id="+temid,null);
	  				if(parHeads.size()>0){
	  					for(BEAMTemparamHead head:parHeads){
	  						List<BEAMTemparamPart> parts=temparamPartService.getTemparamParts("headId.id="+head.getId(), null);
	  						if(parts.size()>0){
	  							for(BEAMTemparamPart part:parts){
	  								BEAMBaseinfoParam param=new BEAMBaseinfoParam();
	  								param.setParamId(part.getParamId());
	  								param.setIsModFlag(part.getIsModFlag());
	  								param.setEamId(baseInfo);
	  								param.setParamType(part.getParamType());
	  								param.setParamValue(part.getParamValue());
	  								param.setUnit(part.getUnit());
	  								baseinfoParamDao.merge(param);
	  							}
	  						}
	  					}
	  				}
	  		 }
	  		 //如果没有点击检查
	  		 if(yeqian.indexOf("jiancha")<0){
	  				List<BEAMTempletBaseCheck> baseCheckList = templetBaseCheckService.getTempletBaseChecks("templetID.id="+temid,null);
	  			    if(baseCheckList.size()>0){
	  			    	for(BEAMTempletBaseCheck tem:baseCheckList){
	  			    		BEAMBaseCheck bc=new BEAMBaseCheck();
	  			    		bc.setAllowDefer(tem.getAllowDefer());
	  			    		bc.setIsSync(true);
	  			    		bc.setTempletID(temid);
	  			    		bc.setPeriodTime(tem.getPerTime());
	  			    		bc.setMaxDefer(tem.getMaxAddTime());
	  			    		bc.setTemCheckId(Integer.parseInt(tem.getId().toString()));
	  			    		bc.setEamID(baseInfo);
	  			    		bc.setBusiType(jian);
	  			    		bc.setBusiProperty(new SystemCode("BEAM010/01"));
	  			    		bc.setStaffID(tem.getStaffID());
	  			    		bc.setDeptID(tem.getExeDept());
                            bc.setOrganization(tem.getOrganization());
	  			    		bc.setPositionID(tem.getPositionID());
	  			    		bc.setPeriodType(tem.getPeriodType());
	  			    		bc.setPeriodUnit(tem.getPeriodUnit());
	  			    	    bc.setMaxDeferUnit(tem.getAddTimeUnit());
	  			    	    bc.setSideType(tem.getSideType());
	  			    	    bc.setWayType(tem.getWayType());
	  			    	    bc.setWorkID(tem.getWorkId());
	  			    	    baseCheckDao.merge(bc);

	  			    	}
	  			    }

	  			    baseCheckDao.flush();
	  		 }

	  		 if(yeqian.indexOf("dianjian")<0){
	  			saveJWXItem( temid, baseInfo, dian,"BEAM010/02");
	  		 }
	  		 if(yeqian.indexOf("runhua")<0){
	  			saveJWXItem( temid, baseInfo, run,"BEAM010/03");
	  		 }

	  		 if(yeqian.indexOf("weixiu")<0){
	  			saveJWXItem( temid, baseInfo, wei,"BEAM010/05");
	  		 }

	  		 if(yeqian.indexOf("baoyang")<0){
	  			saveJWXItem( temid, baseInfo, bao,"BEAM010/04");
	  		 }


	  		}*/

        //对检维修表中的业务类型进行赋值
        List<BEAMJWXItem> items= jWXItemService.getJWXItems("eamID="+baseInfo.getId(), null);
        updateJWXItem(items,dian,run, wei, bao);

        //对检查表中的业务类型进行赋值
        List<BEAMBaseCheck> checks= baseCheckService.getBaseChecks("eamID="+baseInfo.getId(), null);
        updateBaseCheck(checks, jian);

        //处理检查业务模板参照过来的数据模板
        //saveBaseCheckItem(checks, baseInfo);

        //处理设备照片是否删除信息
        if (baseInfo.getDeletePicFlag()) {
            //搜索符合要求数据，排除系统数据错误
            String sql = "select ID FROM base_document WHERE LINK_ID = ? AND TYPE = 'BEAM_baseInfo_baseInfo' AND VALID = 1";
            List<BigDecimal> idList = baseInfoDao.createNativeQuery(sql, baseInfo.getId()).list();

            //删除数据
            for (BigDecimal id : idList) {
                documentService.delete(Long.valueOf(id.toString()));
            }
        }

        //处理复制档案时，没有点击的页签
        if(  baseInfo.getVersion()==0  &&  baseInfo.getIsCopy() && baseInfo.getEamid()!=null ){
            Integer eamid=baseInfo.getEamid();
            saveForCopy( baseInfo, eamid);
        }

        boolean isPotrol = consulService.getValueAsBoolean("platform/bap/EAM/EAM.potrolControl");
        ServiceReference ref = bundleContext.getServiceReference(MobileApiService.class.getName());
        if(null != ref && isPotrol) {
            MobileApiService mobileApiService = (MobileApiService) bundleContext.getService(ref);
            mobileApiService.fromMobileToEam(baseInfo.getId());
        }

        //根据检查业务更新检查方式、内外检，有效期（回填有效期最小的）
        String checkSql = "SELECT NEXT_TIME,WAY_TYPE,SIDE_TYPE FROM BEAM_BASE_CHECKS WHERE EAMID = ? AND NEXT_TIME IS NOT NULL and valid = 1 order by NEXT_TIME";
        List<Object[]> checkResult = baseCheckDao.createNativeQuery(checkSql,baseInfo.getId()).list();
        if(checkResult != null && checkResult.size() > 0){
            String updateSql = "update EAM_BaseInfo set VALID_DATE = ?,WAY_TYPE = ?,SIDE_TYPE = ? where EAM_ID = ?";
            baseInfoDao.createNativeQuery(updateSql,checkResult.get(0)[0],checkResult.get(0)[1],checkResult.get(0)[2],baseInfo.getId()).executeUpdate();
            baseInfoDao.flush();
            baseInfoDao.clear();
        } else { // 如果数组为空，则置空数据
            String updateSql = "update EAM_BaseInfo set VALID_DATE = null,WAY_TYPE = null,SIDE_TYPE = null where EAM_ID = ?";
            baseInfoDao.createNativeQuery(updateSql, baseInfo.getId()).executeUpdate();
            baseInfoDao.flush();
            baseInfoDao.clear();
        }
/* CUSTOM CODE END */
	}
	
	@Override
	public void beforeExcelImportBaseInfo(List<BEAMBaseInfo> baseInfos, Map<String, Map<Integer, Map<Integer, String>>> errors){
	/* CUSTOM CODE START(serviceimpl,beforeExcelImport,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	
	@Override
	public void afterExcelImportBaseInfo(List<BEAMBaseInfo> baseInfos){
	/* CUSTOM CODE START(serviceimpl,afterExcelImport,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	
	@Override
	public void beforeExcelBatchImportBaseInfo(List<BEAMBaseInfo> baseInfos, Map<Integer, Map<Integer, String>> errors){
	/* CUSTOM CODE START(serviceimpl,beforeExcelBatchImport,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */	
	}
	
	@Override
	public void afterExcelBatchImportBaseInfo(List<BEAMBaseInfo> baseInfos){
	/* CUSTOM CODE START(serviceimpl,afterExcelBatchImport,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */	
	}
	
	private void beforeDeleteBaseInfo(BEAMBaseInfo baseInfo){
	/* CUSTOM CODE START(serviceimpl,beforeDelete,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码
        //缺陷
        String sqlFalut = "select count(a.ID) from BEAM2_FAULT_INFOS a where a.valid = 1 and a.status > 0 and a.EAMID = ?";
        Number countFalut = (Number)baseInfoDao.createNativeQuery(sqlFalut,baseInfo.getId()).uniqueResult();
        if(countFalut.longValue()>0){
            throw new BAPException("该设备已被隐患单引用，无法删除！");//该设备存在于缺陷业务中
        }
        //工单
        String sqlRepairRecord = "select count(a.ID) from BEAM2_WORK_RECORDS a where a.valid = 1 and a.status > 0 and a.EAMID = ?";
        Number countRepairRecord = (Number)baseInfoDao.createNativeQuery(sqlRepairRecord,baseInfo.getId()).uniqueResult();
        if(countRepairRecord.longValue()>0){
            throw new BAPException("该设备已被工单引用，无法删除！");//该设备存在于维修业务中
        }
		/*//测量设备
		if(baseInfo.getIsMea() == true){
			//月周检计划
			String sqlCheckMea = "select count(a.ID) from MSBEAM_MEA_PLAN_PARTS a,MSBEAM_MEA_PLANS b where a.HEAD_ID = b.ID and a.valid = 1 and b.valid = 1 and b.status > 0 and a.EAMID = ?";
			Number countCheckMea = (Number)baseInfoDao.createNativeQuery(sqlCheckMea,baseInfo.getId()).uniqueResult();
			//检定记录
			String sqlCheckMeaRep = "select count(ID) from  MSBEAM_MEA_REPORT_INFOS where valid = 1 and status > 0 and EAMID = ?";
			Number countCheckMeaRep = (Number)baseInfoDao.createNativeQuery(sqlCheckMeaRep,baseInfo.getId()).uniqueResult();
            //延期申请
			String sqlCheckMeaDelay = "select count(a.ID) from MSBEAM_DELAY_PARTS a,MSBEAM_DELAY_INFOS b where a.HEADID = b.ID and a.valid = 1 and b.valid = 1 and b.status > 0 and a.EAMID = ?";
			Number countCheckMeaDelay = (Number)baseInfoDao.createNativeQuery(sqlCheckMeaDelay,baseInfo.getId()).uniqueResult();
			if(countCheckMea.longValue()>0 || countCheckMeaRep.longValue()>0 || countCheckMeaDelay.longValue() > 0){
				throw new BAPException("该设备已被检查业务引用，无法删除！");//该设备存在于检查业务中
			}
		}
		//特种设备
		if(baseInfo.getIsSpecialNew() == true){
			//月周检计划
			String sqlCheckSpec = "select count(a.ID) from SBEAM_SPECIAL_PLAN_PARTS a,SBEAM_SPECIAL_PLANS b where a.HEADID = b.ID and a.valid = 1 and b.valid = 1 and b.status > 0 and a.EAMID = ?";
			Number countCheckSpec = (Number)baseInfoDao.createNativeQuery(sqlCheckSpec,baseInfo.getId()).uniqueResult();
			//检定记录
			String sqlCheckSpecRep = "select count(ID) from  SBEAM_SPECIAL_REPORTS where valid = 1 and status > 0 and EAMID = ?";
			Number countCheckSpecRep = (Number)baseInfoDao.createNativeQuery(sqlCheckSpecRep,baseInfo.getId()).uniqueResult();
            //延期申请
			String sqlCheckSpecDelay = "select count(a.ID) from SBEAM_SPECIAL_DELAY_PARTS a,SBEAM_SPECIAL_DELAY_INFOS b where a.HEADID = b.ID and a.valid = 1 and b.valid = 1 and b.status > 0 and a.EAMID = ?";
			Number countCheckSpecDelay = (Number)baseInfoDao.createNativeQuery(sqlCheckSpecDelay,baseInfo.getId()).uniqueResult();
			if(countCheckSpec.longValue()>0 || countCheckSpecRep.longValue()>0 || countCheckSpecDelay.longValue() > 0){
				throw new BAPException("该设备已被检查业务引用，无法删除！");//该设备存在于检查业务中
			}
		}*/
/* CUSTOM CODE END */
	}
	
	private void afterDeleteBaseInfo(BEAMBaseInfo baseInfo){
	/* CUSTOM CODE START(serviceimpl,afterDelete,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码
        //将删除的设备编码增加“delete_时间”
        Date deleteDate = new Date();
        long dates = deleteDate.getTime();
        String deleteCode = baseInfo.getCode() + "_delete_" + String.valueOf(dates);
        baseInfo.setCode(deleteCode);
        baseInfoDao.save(baseInfo);
/* CUSTOM CODE END */
	}
	
	private void beforeRestoreBaseInfo(BEAMBaseInfo baseInfo){
	/* CUSTOM CODE START(serviceimpl,beforeRestore,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	
	private void afterRestoreBaseInfo(BEAMBaseInfo baseInfo){
	/* CUSTOM CODE START(serviceimpl,afterRestore,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	
	private void customGenerateCodes(BEAMBaseInfo baseInfo, Object... objects) {
		/* CUSTOM CODE START(serviceimpl,customGenerateCodes,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	private void customGenerateSummarys(BEAMBaseInfo baseInfo, Object... objects) {
		/* CUSTOM CODE START(serviceimpl,customGenerateCodes,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}

	private void beforeServiceDestroy() {
	/* CUSTOM CODE START(serviceimpl,beforeServiceDestroy,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}

	private void beforeInitBean(){
		/* CUSTOM CODE START(serviceimpl,beforeInitBean,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	private void afterInitBean(){
		/* CUSTOM CODE START(serviceimpl,afterInitBean,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	@Override
	public void beforeImportBaseInfo(List<BEAMBaseInfo> insertObjs, List<BEAMBaseInfo> updateObjs, 
			List<Map<String,String>> columnInfo,List<Map<String, Map<Integer, Map<Integer, String>>>> errMsgSheet, Map<String ,List<Map<String, Object>>> importNodeInfo, Map<String, Property> importPropInfo){//主辅模型导入before方法
		/* CUSTOM CODE START(serviceimpl,beforeImportBaseInfo,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	@Override
	public void afterImportBaseInfo(List<BEAMBaseInfo> insertObjs, List<BEAMBaseInfo> updateObjs, 
			List<Map<String,String>> columnInfo, Map<String ,List<Map<String, Object>>> importNodeInfo, Map<String, Property> importPropInfo){//主辅模型导入after方法
		/* CUSTOM CODE START(serviceimpl,afterImportBaseInfo,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码

/* CUSTOM CODE END */
	}
	/* CUSTOM CODE START(serviceimpl,functions,BEAM_1.0.0_baseInfo_BaseInfo,BEAM_1.0.0) */
// 自定义代码
    @Override
    public String haveWorkOrI(String eamID){//判断该设备是否被作业或者作业项所引用
        String flag="false";
        try {
            String sql="select count(a.ID) from MOBILEEAM_work_Items a ,MOBILEEAM_WORK_BEAMS b where a.valid=1 and a.EAMID=?  or b.valid=1 and b.BEAM=?";
            Number count = (Number)baseInfoDao.createNativeQuery(sql,eamID,eamID).uniqueResult();
            if(count.longValue()>0){
                flag="true";//该点检业务存在于免检规则
            }
        }
        catch(Exception e)
        {
            flag="false";
        }
        return flag;
    }

    @Override
    public String isExeRule(String jwxID){//判断jwxID是否制定了免检计划
        String flag="false";
        try {
            String sql="select count(ID) from MOBILEEAM_ITEM_CONCLUSES where ITEMID in(select ID from MOBILEEAM_WORK_ITEMS where JWX_ITEMID=?) or NEXT_ITEMID in(select ID from MOBILEEAM_WORK_ITEMS where JWX_ITEMID=?)";
            Number conslusesList=(Number)baseInfoDao.createNativeQuery(sql,jwxID,jwxID).uniqueResult();
            if(conslusesList.longValue()>0){
                flag="true";//该点检业务存在于免检规则
            }
        }
        catch(Exception e)
        {
            flag="false";
        }
        return flag;
    }





    @Override
    public String isLeaf(String eamID) {
        // TODO Auto-generated method stub
        String flag="";
        String sql="select * from EAM_EAMTYPE  where LEAF='0' and EAMTYPE_ID="+eamID;
        List<Object[]> eamTypeList=baseInfoDao.createNativeQuery(sql).list();
        if(eamTypeList.size()>0){
            flag="0";
        }
        return flag;
    }
    //查看用户是否有业务权限
    @Override
    public String hasEamtypePower(String userId, String operation, String eamID) {
        // TODO Auto-generated method stub
        String eamTypeCode=eamTypeService.getEamType(Long.parseLong(eamID)).getCode();
        String  flag="false";
        String sqlContent = "SELECT content FROM BASE_USER_SPECIALPERMISSION WHERE SPECIAL_PERMISSION_CODE='BEAM_1.0.0_BEAM_1.0.0_baseInfo_BaseInfo_eamType' and USERPERMISSION_ID IN (SELECT id FROM BASE_USERPERMISSION WHERE user_id = ? AND NO_RESTRICT_FLAG = '0' AND MENUOPERATE_id IN (SELECT id FROM BASE_MENUOPERATE WHERE code = ?))";
        List<Object> contentList=baseInfoDao.createNativeQuery(sqlContent,userId,operation).list();
        String sqlContentDeptAndUser = "SELECT content FROM BASE_USER_SPECIALPERMISSION WHERE (SPECIAL_PERMISSION_CODE='BEAM_1.0.0_BEAM_1.0.0_baseInfo_BaseInfo_useDept' or SPECIAL_PERMISSION_CODE='BEAM_1.0.0_BEAM_1.0.0_baseInfo_BaseInfo_dutyStaff') and USERPERMISSION_ID IN (SELECT id FROM BASE_USERPERMISSION WHERE user_id = ? AND NO_RESTRICT_FLAG = '0' AND MENUOPERATE_id IN (SELECT id FROM BASE_MENUOPERATE WHERE code = ?))";
        List<Object> contentDeptAndUserList=baseInfoDao.createNativeQuery(sqlContentDeptAndUser,userId,operation).list();
        String isRestictSql="SELECT id FROM BASE_USERPERMISSION WHERE user_id = ? AND NO_RESTRICT_FLAG = '1' AND MENUOPERATE_id IN (SELECT id FROM BASE_MENUOPERATE WHERE code = ?)";
        List<Object> isRestictList=baseInfoDao.createNativeQuery(isRestictSql,userId,operation).list();
        if(isRestictList.size()>0){
            flag="true";
        }
        if(contentDeptAndUserList.size()>0){
            flag="true";
        }
		/*if(contentList.size()>0){
			if(contentList.get(0)!=null){
				flag="false";
				String[] contents=contentList.get(0).toString().split(",");
				for(String content:contents){
					content=content.replace("'", "");
					if(eamTypeCode.equals(content)){
						flag="true";
					}
				}
			}

		}*/
        if(contentList.size()>0){
            //sp03后content不是直接存值了，而是存了一句sql语句，具体的值需要执行sql后获得
            for (int i = 0; i < contentList.size(); i++) {
                List<Object> eamTypeCodes = eamTypeDao.createNativeQuery((String)contentList.get(i)).list();
                if(eamTypeCodes.size() > 0){
                    flag="false";
                    //String[] contents=contentList.get(0).toString().split(",");
                    for(Object typeCode:eamTypeCodes){
                        if(eamTypeCode.equals((String)typeCode)){
                            flag="true";
                            break;
                        }
                    }
                }
                if (flag == "true") {
                    break;
                }
            }
        }
        return flag;
    }
    //更新建档标记
    @Override
    public String updateFileState(String fileState, String baseInfoIds) {
        // TODO Auto-generated method stub
        String flag="true";
        baseInfoIds=baseInfoIds.substring(1);
        String[] baseInfoIdList =baseInfoIds.split(",");
        for(String baseInfoId:baseInfoIdList){
            BEAMBaseInfo baseInfo=new BEAMBaseInfo();
            baseInfo=getBaseInfo(Long.parseLong(baseInfoId));
            SystemCode fileStateS=new SystemCode(fileState);
            if("BEAM038/01".equals(fileState)){
                baseInfo.setFileDate(new Date());
            }
            if("BEAM038/03".equals(fileState)){
                baseInfo.setFileDate(null);
            }
            baseInfo.setFileState(fileStateS);
            //mergeBaseInfo(baseInfo, null);
            baseInfoDao.merge(baseInfo);
        }
        return flag;
    }

    //对检查业务的项目进行维护
    public String insertCheckItemData(String checkid, String datastr) {
        if(!"".equals(checkid)){
            String sql="delete from BEAM_BASE_CHECK_ITEMS where BASE_CHECK=" + checkid;
            baseInfoDao.createNativeQuery(sql).executeUpdate();
        }
        String[] pointPlans=datastr.split(";");
        if(!"".equals(datastr)&&!"".equals(checkid)){
            for(String s:pointPlans){
                String[] sp=s.split(",");
                //检查业务
                BEAMBaseCheck bc=baseCheckService.getBaseCheck(Long.parseLong(checkid));

                BEAMBaseCheckItem bitem=new BEAMBaseCheckItem();
              /*
  				if(!"undefined".equals(sp[0]) && !"".equals(sp[0])){
  				//公用项目
  	  			BEAMPublicItem publicItem=publicItemService.getPublicItem(Long.parseLong(sp[0]));
  				bitem.setPublicItemID(publicItem);
  				}*/
                if(!"undefined".equals(sp[1])){
                    bitem.setQstandard(sp[1]=="undefined"?"":sp[1]);
                }
                if(!"undefined".equals(sp[2])){
                    bitem.setRemark(sp[2]=="undefined"?"":sp[2]);
                }
                //设备档案
                if(!"undefined".equals(sp[3]) && sp[3] != null){
                    BEAMBaseInfo baseInfo = getBaseInfo(Long.valueOf(sp[3]));
                    bitem.setEamID(baseInfo);
                }
                bitem.setBaseCheck(bc);
                //bitem.setEamID(eam);
                if(!"undefined".equals(sp[4])){
                    bitem.setTempletItemID(Integer.valueOf(sp[4]));
                }
                if(!"undefined".equals(sp[5])){
                    bitem.setContent(sp[5]);
                }
                if(!"undefined".equals(sp[6])){
                    bitem.setClaim(sp[6]);
                }
                baseCheckItemDao.merge(bitem);
            }
        }
        return "";
    }

    //对检维修中的业务类型进行更新
    public void updateJWXItem(List<BEAMJWXItem> items,BEAMBusitype dian,BEAMBusitype run,BEAMBusitype wei,BEAMBusitype bao){

        for(BEAMJWXItem i:items ){
            String pro="";
            if(i!=null && i.getBusiProperty()!=null && i.getBusiProperty().getId()!=null ){

                pro=i.getBusiProperty().getId().toString();
                //点检
                if(pro!=null && pro.equals("BEAM010/02")  &&  dian!=null ){
                    i.setBusiTypeID(dian);
                    jWXItemDao.merge(i);
                }
                //润滑
                if(pro!=null && pro.equals("BEAM010/03") && run!=null){
                    i.setBusiTypeID(run);
                    jWXItemDao.merge(i);
                }
                //维修
                if(pro!=null && pro.equals("BEAM010/05") && wei!=null){
                    i.setBusiTypeID(wei);
                    jWXItemDao.merge(i);
                }
                //保养
                if(pro!=null && pro.equals("BEAM010/04") && bao!=null){
                    i.setBusiTypeID(bao);
                    jWXItemDao.merge(i);
                }

            }
        }

    }

    //对检查中的业务性质进行更新
    public void updateBaseCheck(List<BEAMBaseCheck> checks,BEAMBusitype jian){
        for(BEAMBaseCheck b:checks){
            String pro="";
            if(b!=null && b.getBusiProperty()!=null && b.getBusiProperty().getId()!=null ){
                if(pro!=null && pro.equals("BEAM010/04") && jian!=null){
                    b.setBusiType(jian);
                    baseCheckDao.merge(b);
                }
            }
        }
    }
/*

 //保存检查业务的明细
 public void saveBaseCheckItem(List<BEAMBaseCheck> checks,BEAMBaseInfo baseInfo){
		for(BEAMBaseCheck b:checks){
			Integer tempId=b.getTempletID();
			   //如果数据是参照模板过来的数据
			   if(tempId!=null && tempId>0){
				   Integer size=  baseCheckItemService.getBaseCheckItems("baseCheck="+b.getId(), null).size();
				   if(size<=0){
				   BEAMTempletBaseCheck temcheck=templetBaseCheckService.getTempletBaseChecks("templetID="+tempId, null).get(0);
					   if(temcheck!=null){
						  List<BEAMTemBaseCheckItem> parts= temBaseCheckItemService.getTemBaseCheckItems("baseCheckid="+temcheck.getId(), null);
						  for(BEAMTemBaseCheckItem p:parts ){
							  BEAMBaseCheckItem bi=new BEAMBaseCheckItem();
							  bi.setPublicItemID(p.getPublicItemId());
							  bi.setClaim(p.getClaim());
							  bi.setContent(p.getContent());
							  bi.setQstandard(p.getQstandard());
							  bi.setBaseCheck(b);
							  bi.setEamID(baseInfo);
							  bi.setTempletItemID(Integer.valueOf(p.getId().toString()));
							  baseCheckItemDao.merge(bi);
						  }

					   }

				   }
			   }
		}
 }*/
 /*
 //新增设备档案时，处理参照了模板，但是没有点击相关页签的情况
 public void saveJWXItem(Integer temid,BEAMBaseInfo baseInfo,BEAMBusitype dian,String busiCode){
		List<BEAMTempletJWXHead> headList = templetJWXHeadService.getTempletJWXHeads("templetID.id="+temid,null);
		if(headList.size()>0){
			for(BEAMTempletJWXHead head:headList){
			  String pro=head.getBusiTypeID().getProperty().getId(); //业务性质
			  if(pro.equals(busiCode)){
				List<BEAMTempletJWXItemPart> itemList = templetJWXItemPartService.getTempletJWXItemParts("headid.id="+head.getId(), null);
				if(itemList.size()>0){
					for(BEAMTempletJWXItemPart part:itemList){
					  BEAMJWXItem i=new	BEAMJWXItem();
					  i.setBusiTypeID(dian);
					  i.setBusiProperty(new SystemCode(busiCode));
					  i.setClaim(part.getItemClaim());
					  //i.setContent(part.getContent());
                      i.setContent(part.getItemContent());//内容
					  i.setDeptID(part.getDeptID());
					  i.setEamID(baseInfo);
					  i.setIsSync(true);
					  i.setOrganization(part.getOrganization());
					  i.setPeriod(part.getPeriodTime());
					  i.setPeriodType(part.getPeriodType());
					  i.setPeriodUnit(part.getPeriodUnit());
					  i.setPositionID(part.getPositionID());
					  i.setProductID(part.getProductID());
					  i.setPublicItemID(part.getPublicItemID());
					  i.setStaffID(part.getStaffID());
					  i.setStartCon(part.getStartCon());
					  i.setSum(part.getSum());
					  i.setTempItemID(Integer.parseInt(part.getId().toString()));
					  i.setTempletID(temid);
					  i.setUnitName(part.getUnitName());
					  i.setWorkID(part.getWorkId());
                      //点检相关字段和某些未保存的字段
					  i.setInputStandardID(part.getInputStandardID());//录入标准
					  i.setAutoJudge(part.getAutoJudge());//自动判断
					  i.setNormalRange(part.getNormalRange());//正常范围
					  i.setLimitValue(part.getLimitValue());//上限值
					  i.setLlimitValue(part.getLlimitValue());//下限值
					  i.setIsThermometric(part.getIsThermometric());//测温
					  i.setIsSeismic(part.getIsSeismic());//测震
					  i.setIsphone(part.getIsphone());//拍照
					  i.setIspass(part.getIspass());//允许跳过
					  i.setControl(part.getControl());//结论修改
					  i.setAutoGetValue(part.getAutoGetValue());//自动采集
					  i.setAutoAanalysis(part.getAutoAanalysis());//对标分析
					  i.setRemark(part.getRemark());//备注
					  jWXItemDao.merge(i);
					}
				}
			  }
			}
		}
 }*/


    //处理复制档案的时候，处理没有点击的页签的数据
    public void saveForCopy(BEAMBaseInfo baseInfo,Integer eamid){
        String yeqian=baseInfo.getPageProperty();
        //如果没有点击备品备件页签
        if(yeqian.indexOf("beijianqingdan")<0){
            List<BEAMSparePart> beis= sparePartService.getSpareParts("eamID="+eamid, null);
            if(beis.size()>0){
                for(BEAMSparePart bei:beis ){
                    BEAMSparePart b=new BEAMSparePart();
                    b.setEamID(baseInfo);
                    b.setDepleteSum(bei.getDepleteSum());
                    b.setProductID(bei.getProductID());
                    b.setSpareMemo(bei.getSpareMemo());
                    sparePartDao.merge(b);
                }
            }
        }
        //如果没有点击设备文档页签

 /*
 if(yeqian.indexOf("shebeiwendang")<0){
	  List<BEAMDocPart> docs=docPartService.getDocParts("eamID="+eamid, null);
	  if(docs.size()>0){
		 for(BEAMDocPart doc:docs){
			 BEAMDocPart d=new BEAMDocPart();
			 d.setCode(doc.getCode());
			 d.setDocAddress(doc.getDocAddress());
			 d.setDocType(doc.getDocType());
			 d.setEamID(baseInfo);
			 d.setName(doc.getName());
			 d.setRemark(doc.getRemark());
			 d.setSum(doc.getSum());
			 d.setSummary(doc.getSummary());
			 docPartDao.merge(d);
		 }
	  }
 }
 */

//如果没有点击技术参数
        if(yeqian.indexOf("jishucanshu")<0){
            List<BEAMBaseinfoParam> pams=baseinfoParamService.getBaseinfoParams("eamId="+eamid, null);
            if(pams.size()>0){
                for(BEAMBaseinfoParam p :pams){
                    BEAMBaseinfoParam param=new BEAMBaseinfoParam();
                    //param.setParamId(p.getParamId());
                    param.setIsModFlag(p.getIsModFlag());
                    param.setEamId(baseInfo);
                    param.setParamType(p.getParamType());
                    param.setParamValue(p.getParamValue());
                    param.setUnit(p.getUnit());
                    baseinfoParamDao.merge(param);
                }
            }
        }

        //如果没有点击检查业务
        if(yeqian.indexOf("jiancha")<0){
            List<BEAMBaseCheck> bcs= baseCheckService.getBaseChecks("eamID="+eamid, null) ;
            if(bcs.size()>0){
                for(BEAMBaseCheck tem:bcs){
                    BEAMBaseCheck bc=new BEAMBaseCheck();
                    bc.setAllowDefer(tem.getAllowDefer());
                    bc.setIsSync(tem.getIsSync());
                    bc.setTempletID(tem.getTempletID());
                    bc.setPeriodTime(tem.getPeriodTime());
                    bc.setMaxDefer(tem.getMaxDefer());
                    bc.setTemCheckId(tem.getTemCheckId());
                    bc.setEamID(baseInfo);
                    bc.setBusiType(tem.getBusiType());
                    bc.setBusiProperty(tem.getBusiProperty());
                    bc.setStaffID(tem.getStaffID());
                    bc.setDeptID(tem.getDeptID());
                    bc.setPositionID(tem.getPositionID());
                    bc.setOrganization(tem.getOrganization());
                    bc.setPeriodUnit(tem.getPeriodUnit());
                    bc.setMaxDeferUnit(tem.getMaxDeferUnit());
                    bc.setSideType(tem.getSideType());
                    bc.setWayType(tem.getWayType());
                    // bc.setWorkID(tem.getWorkID());
                    baseCheckDao.merge(bc);
                }
                baseCheckDao.flush();
            }
            //处理检查业务明细
            List<BEAMBaseCheck> checks= baseCheckService.getBaseChecks("eamID="+baseInfo.getId(), null) ;
            //saveBaseCheckItem( checks, baseInfo);
        }


        if(yeqian.indexOf("dianjian")<0){
            saveJWXCopy( baseInfo, eamid, "BEAM010/02");
        }
        if(yeqian.indexOf("runhua")<0){
            saveJWXCopy( baseInfo, eamid, "BEAM010/03");
        }

        if(yeqian.indexOf("weixiu")<0){
            saveJWXCopy( baseInfo, eamid, "BEAM010/05");
        }

        if(yeqian.indexOf("baoyang")<0){
            saveJWXCopy( baseInfo, eamid, "BEAM010/04");
        }



    }

    //保存复制设备档案时，没有点击的检维修项目的数据
    public void saveJWXCopy(BEAMBaseInfo baseInfo,Integer eamid,String busiCode){
        List<BEAMJWXItem> jwxs= jWXItemService.getJWXItems("eamID="+eamid, null);
        if(jwxs.size()>0){
            for(BEAMJWXItem part:jwxs){
                String pro= "";
                if(part.getBusiTypeID()!=null ){
                    pro= part.getBusiTypeID().getProperty().getId();
                }
                if(part.getBusiProperty()!=null){
                    pro= part.getBusiProperty().getId();
                }
                if(pro.equals(busiCode)){
                    BEAMJWXItem i=new	BEAMJWXItem();
                    i.setBusiTypeID(part.getBusiTypeID());
                    i.setBusiProperty(part.getBusiProperty());
                    i.setClaim(part.getClaim());
                    i.setContent(part.getContent());
                    i.setDeptID(part.getDeptID());
                    i.setEamID(baseInfo);
                    i.setIsSync(part.getIsSync());
                    i.setOrganization(part.getOrganization());
                    i.setPeriod(part.getPeriod());
                    i.setPeriodType(part.getPeriodType());
                    i.setPeriodUnit(part.getPeriodUnit());
                    i.setPositionID(part.getPositionID());
                    i.setProductID(part.getProductID());
                    //  i.setPublicItemID(part.getPublicItemID());
                    i.setStaffID(part.getStaffID());
                    i.setStartCon(part.getStartCon());
                    i.setSum(part.getSum());
                    i.setTempItemID(part.getTempItemID());
                    i.setTempletID(part.getTempletID());
                    i.setUnitName(part.getUnitName());
                    //i.setWorkID(part.getWorkID());
                    jWXItemDao.merge(i);
                }
            }
        }
    }
    //设备数据库修改显示
    public void deleteParam(Long jwxitemID){
        String sql="update BEAM_JWXITEMS set VALID=0 where ID=?";
        baseInfoDao.createNativeQuery(sql,jwxitemID).executeUpdate();
    }//检维修项目表
    public void deletebaseCheck(Long baseCheckId){
        String sql="update BEAM_BASE_CHECKS set VALID=0 where ID=?";
        baseInfoDao.createNativeQuery(sql,baseCheckId).executeUpdate();
    }//检查表
    @Resource
    private ConsulService consulService;

    private Boolean showControlValue;

    @Autowired
    private DepartmentService deptService;

    protected String getCustomCondition( Page page, String viewCode, int type, String processKey, List<Param> params, List<Object> list){
        String sqlCustConditon="";
        //档案维护、建档申请、 建档审批、  档案查看、设备台账
        if ("BEAM_1.0.0_baseInfo_baseInfoPartToCFS".equals(viewCode) || "BEAM_1.0.0_baseInfo_baseInfoPartToApp".equals(viewCode) ||"BEAM_1.0.0_baseInfo_baseInfoPartToAppro".equals(viewCode)||"BEAM_1.0.0_baseInfo_baseInfoPartForview".equals(viewCode) || "BEAM_1.0.0_baseInfo_baseInfoPartPerport".equals(viewCode)) {
            showControlValue = consulService.getValueAsBoolean("platform/bap/EAM/EAM.showControl");
            if (showControlValue) {

            }else{
                sqlCustConditon = "EAM_ISMEA <> 1 and IS_SPECIAL_NEW <> 1 and ";
            }

        }
        if ( "BEAM_1.0.0_baseInfo_baseInfoPartToCFS".equals(viewCode) || "BEAM_1.0.0_baseInfo_baseInfoPartToApp".equals(viewCode) || "BEAM_1.0.0_baseInfo_baseInfoPartToAppro".equals(viewCode)  || "BEAM_1.0.0_baseInfo_baseInfoForWorkflow".equals(viewCode)  ){
            //获取档案修改过滤条件
            String eamIDs = powerHeadService.checkLevel("BEAM060/01",null);
            sqlCustConditon+=  eamIDs ;
        }
        if("BEAM_1.0.0_baseInfo_baseInfoPartForview".equals(viewCode) || "BEAM_1.0.0_baseInfo_baseInfoPartPerport".equals(viewCode)){
            //获取档案查看过滤条件
            String eamIDs = powerHeadService.checkLevel("BEAM060/02",null);
            sqlCustConditon+=  eamIDs ;
        }
        //测量设备查看和台账
        if("BEAM_1.0.0_baseInfo_meaBaseInfoPartToAp".equals(viewCode) || "BEAM_1.0.0_baseInfo_meaBaseInfoPartToCFS".equals(viewCode) || "BEAM_1.0.0_baseInfo_meaBaseInfoPartView".equals(viewCode)
                ||	"BEAM_1.0.0_baseInfo_meaBaseInfoPerport".equals(viewCode) || "BEAM_1.0.0_baseInfo_meaBaseInfoPartToApp".equals(viewCode) ){
            String eamIDs = powerHeadService.checkLevel("BEAM060/11",null);
            sqlCustConditon+=  eamIDs ;
        }
        //特种设备档案和查看
        if("BEAM_1.0.0_baseInfo_speBaseInfoPartView".equals(viewCode) || "BEAM_1.0.0_baseInfo_speBaseInfoPerport".equals(viewCode) || "BEAM_1.0.0_baseInfo_speBaseInfoPartToAp".equals(viewCode) ||
                "BEAM_1.0.0_baseInfo_speBaseInfoPartToApp".equals(viewCode) || "BEAM_1.0.0_baseInfo_speBaseInfoPartToCFS".equals(viewCode)){
            String eamIDs = powerHeadService.checkLevel("BEAM060/12",null);
            sqlCustConditon+=  eamIDs ;
        }
        if("BEAM_1.0.0_baseInfo_baseInfoAuthority".equals(viewCode)){
		/*String eamIDs = powerHeadService.checkLevel(page.getErrMsg(),null);
		sqlCustConditon+=  eamIDs ;*/
            String[] paramMsgs = null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                paramMsgs = page.getErrMsg().split(",");
            }
            if (paramMsgs != null) {
                //按人的权限过过滤
                String powerType = paramMsgs[0];
                if(paramMsgs[0]!=null &&!"".equals(paramMsgs[0]) && !"null".equals(paramMsgs[0])){
                    powerType = paramMsgs[0];
                }
                Long userId = null;
                if(paramMsgs[1]!=null &&!"".equals(paramMsgs[1]) && !"null".equals(paramMsgs[1])){
                    userId = Long.parseLong(paramMsgs[1]);
                }
                String eamIDs = powerHeadService.checkLevel(powerType,userId);
                sqlCustConditon+=  eamIDs ;
                //不同视图调用，具有不同条件
                if("baseInfoWorkflow".equals(paramMsgs[2])){
                    //档案流程调用
                    sqlCustConditon+= " and \"baseInfo\".EAM_ID not in ( select BEAM_INFO from BEAM_BASE_INFO_WORKFLOWS where status=88 and valid=1 and BEAM_INFO is not null) ";
                }

                //隐患单打开设备参照视图，如果已选区域位置，则需要加入区域位置过滤条件
                if(paramMsgs[4]!=null && !"".equals(paramMsgs[4]) && !"null".equals(paramMsgs[4])){
                    sqlCustConditon += " and \"baseInfo\".INSTALL_PLACE = " + paramMsgs[4] + " ";
                }
            }
        }
        if("BEAM_1.0.0_baseInfo_baseInfoRefRS".equals(viewCode)){
            Long userId=null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                userId=Long.parseLong(page.getErrMsg());
            }
            String eamIDs = powerHeadService.checkLevel("BEAM060/02",userId);
            sqlCustConditon+=  eamIDs ;
        }
        if("BEAM_1.0.0_baseInfo_runningSetRef".equals(viewCode)){
            Long userId=null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                userId=Long.parseLong(page.getErrMsg());
            }
            String eamIDs = powerHeadService.checkLevel("BEAM060/06",userId);
            sqlCustConditon+=  eamIDs ;
        }
        //特种
        if("BEAM_1.0.0_baseInfo_speViewRefPart".equals(viewCode)){
		/*String eamIDs = powerHeadService.checkLevel("BEAM060/12",null);
	    sqlCustConditon+=  eamIDs ;*/
            String[] paramMsgs = null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                paramMsgs = page.getErrMsg().split(",");
            }
            if (paramMsgs != null) {
                String powerType = null;
                if(paramMsgs[0]!=null &&!"".equals(paramMsgs[0]) && !"null".equals(paramMsgs[0])){
                    powerType = paramMsgs[0];
                }
                Long userId = null;
                if(paramMsgs[1]!=null &&!"".equals(paramMsgs[1]) && !"null".equals(paramMsgs[1])){
                    userId = Long.parseLong(paramMsgs[1]);
                }
                String eamIDs = powerHeadService.checkLevel(powerType,userId);
                sqlCustConditon+=  eamIDs ;
            }
        }
        //测量
        if("BEAM_1.0.0_baseInfo_meaViewRefPart".equals(viewCode)){
		/*String eamIDs = powerHeadService.checkLevel("BEAM060/11",null);
	    sqlCustConditon+=  eamIDs ;*/
            String[] paramMsgs = null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                paramMsgs = page.getErrMsg().split(",");
            }
            if (paramMsgs != null) {
                String powerType = null;
                if(paramMsgs[0]!=null &&!"".equals(paramMsgs[0]) && !"null".equals(paramMsgs[0])){
                    powerType = paramMsgs[0];
                }
                Long userId = null;
                if(paramMsgs[1]!=null &&!"".equals(paramMsgs[1]) && !"null".equals(paramMsgs[1])){
                    userId = Long.parseLong(paramMsgs[1]);
                }
                String eamIDs = powerHeadService.checkLevel(powerType,userId);
                sqlCustConditon+=  eamIDs ;
            }
        }
        //特种延期
        if("BEAM_1.0.0_baseInfo_specBaseCheckRef".equals(viewCode)){
            Long userId=null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                userId=Long.parseLong(page.getErrMsg());
            }
            String eamIDs = powerHeadService.checkLevel("BEAM060/12",userId);
            sqlCustConditon+=  eamIDs ;
        }
        //测量延期
        if("BEAM_1.0.0_baseInfo_meaSpecBaseCheckRef".equals(viewCode)){
            Long userId=null;
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                userId=Long.parseLong(page.getErrMsg());
            }
            String eamIDs = powerHeadService.checkLevel("BEAM060/11",userId);
            sqlCustConditon+=  eamIDs ;
        }
        //设备按部门过滤参照
        if("BEAM_1.0.0_baseInfo_baseInfoRef".equals(viewCode)){
            if(page.getErrMsg()!=null &&!"".equals(page.getErrMsg())){
                //取部门层级
                Department departss = (Department)deptService.getDeaprtmentByID(Long.valueOf(page.getErrMsg()));
                String layRec = departss.getLayRec();
                sqlCustConditon += " \"baseInfo\".EAM_USEDEPTID in (select ID from BASE_DEPARTMENT where ( ID = "+ page.getErrMsg() +" or lay_Rec like '" + layRec + "-%' and valid = 1)) " ;
            }
        }
        return sqlCustConditon;
    }
    @Override
    public String getsumss(String productID){
        String numrr="";
        /*
         * @author zhushizhang
         * @reason 当一个新环境没有发布物料模块的时候就无法对物料模块的现存量进行查询
         * 所以在下面加一个模块发布的查询信息来进行判断
         * @time 2017.2.18
         */
        String isExistMaterialModule="select id from EC_DEPLOY_INFO  where MODULE_CODE='material_1.0'";
        List<Object> isExistMaterialModuleList=baseInfoDao.createNativeQuery(isExistMaterialModule).list();
        if(isExistMaterialModuleList.size()>0){
            String strSql="select baseProduct.PRODUCT_CODE codess,sum(materialCrop.STANDINGCROP) nowNum from MATERIAL_CROP_GATHERS materialCrop join S2BASE_PRODUCT baseProduct on materialCrop.GOOD=baseProduct.PRODUCT_ID where baseProduct.VALID=1 and materialCrop.VALID=1 and baseProduct.PRODUCT_CODE in("+productID+") group by baseProduct.PRODUCT_CODE";
            List<Object[]>  productSumList =baseInfoDao.createNativeQuery(strSql.toString()).list();
            Object[] productCodess=productID.split(",");
            for (int i = 0; i < productCodess.length; i++) {
                Object object = productCodess[i];
                for (Object[] productSum:productSumList) {
                    if(("'"+productSum[0]+"'").equals(object)){
                        numrr+=productSum[0]+" standingCrop "+productSum[1]+",";
                        break;
                    }
                }
            }
        }
        return numrr;
    }


    private Connection conn;
    @Override
    public String getProductNum(String codes){

        SQLServerDataSource ds3 = new SQLServerDataSource();
        ds3.setIntegratedSecurity(false);
        ds3.setURL("jdbc:sqlserver://128.128.99.90:1433;DatabaseName=wx_sbgl_zjk");
        ds3.setUser("stll");
        ds3.setPassword("stl95868");
        ds3.setDatabaseName("wx_sbgl_zjk");
        String numrr="";
        try {
            log.info("begin");
            conn =  ds3.getConnection();
            log.info("end");

            Statement stmt = null;
            ResultSet rs = null;
            log.info("start");
            //StringBuilder strSql=new StringBuilder("select fqty from  wx_phjd_kc  where fwlmc in (:codeObj)");
            String SQL = "SELECT [fgs],[fwlbm],[fqty] FROM [wx_sbgl_zjk].[dbo].[wx_phjd_kcb] where [fwlbm] in ("+codes+")";
            log.info(SQL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                numrr+=rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+",";
                log.info(numrr);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return numrr;
    }


    /**
     * @Description 调用卫星的接口数据
     * @param portalUrl 调用的url
     * @author zhangyan
     * @date 2017年8月30日
     * @return 库存量
     */
    @Override
    public String validateUser(String portalUrl,HttpServletRequest request) {
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        GetMethod httpGet = new GetMethod(portalUrl);
        String result = null;
        BufferedInputStream bis = null;
        //执行http请求
        HttpResponse response;
        //模拟登录
        //loginByAuto();
        try {
            Cookie[] cookies = request.getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.getName()+'='+c.getValue() + ";");
            }
            httpGet.setRequestHeader("cookie", tmpcookies.toString());
            httpClient.executeMethod(httpGet);
            result = httpGet.getResponseBodyAsString();
            log.info(" result: " + result);
            Object[] resultObj=result.split(":");
            result=resultObj[1].toString();
            result=result.substring(1, result.length()-2);
            log.info("get validateResult : " + result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Autowired
    private IdGenerator idGenerator;
    /**
     * @Description 复制设备档案
     * @param beamID 被复制的设备ID
     * @param beamCode 新的设备编码
     * @param beamName 新的设备名称
     * @return 复制后的设备ID
     **/
    @Override
    public Long copySaveBeam(Long beamID, String beamCode, String beamName){
        //查询设备编码是否重复
        List<BEAMBaseInfo> baseInfos = findByProperty("code", beamCode);
        if (baseInfos.size() > 0) {
            return -1l;
        }

        //取当前公司ID
        Long CID = getCurrentCompanyId();
        //取表字段
        String baseInfoCol = getCols("BaseInfo");
        //取新的设备ID
        Long eamIDMax = idGenerator.getNextId("EAM_BaseInfo");
        //复制设备
        String copyEamSql = "insert into EAM_BaseInfo (EAM_ID,EAM_CODE,EAM_NAME,IS_COPY,EAMID,CID,VERSION," + baseInfoCol + ") select " + eamIDMax.toString() + ",'" + beamCode + "','" + beamName + "',1," + beamID.toString() + ","+ CID.toString() + ",1," + baseInfoCol
                + " from EAM_BaseInfo where EAM_ID = " + beamID.toString() + " and VALID = 1";
        baseInfoDao.createNativeQuery(copyEamSql).executeUpdate();

        //1.附属设备复制
        String attachPartCol = getCols("AttachPart");
        insertTab("BEAM_ATTACH_PARTS",attachPartCol,beamID,eamIDMax);

        //2.备件清单复制
        String sparePartCol = getCols("SparePart");
        insertTab("BEAM_SPARE_PARTS",sparePartCol,beamID,eamIDMax);

        //3.设备文档复制
        String docPartCol = getCols("DocPart");
        insertTab("BEAM_DOC_PARTS",docPartCol,beamID,eamIDMax);
        //附件字段
        BEAMBaseInfo baseInfoOld = getBaseInfo(beamID);
        BEAMBaseInfo baseInfoNew = getBaseInfo(eamIDMax.longValue());
        String documentCol = "VALID,TYPE,TASK_DESCRIPTION,SHOW_TYPE,PROPERTY_CODE,PATH,OPEN_TIME,OPENER,NAME,MODIFY_TIME,MODIFY_STAFF_ID,MEMO,FILE_TYPE,FILE_SIZE,DEPLOYMENT_ID,DELETE_TIME,DELETE_STAFF_ID,CREATE_TIME,CREATE_STAFF_ID,ACTIVITY_NAME";
        String docPartSqlOld = "from " + BEAMDocPart.JPA_NAME + " where eamID = ?" + " order by ID";
        String docPartSqlNew = "from " + BEAMDocPart.JPA_NAME + " where eamID = ?" + " order by ID";
        List<BEAMDocPart> docPartOlds = docPartDao.findByHql(docPartSqlOld, baseInfoOld);
        List<BEAMDocPart> docPartNews = docPartDao.findByHql(docPartSqlOld, baseInfoNew);
        for (int i = 0; i < docPartOlds.size(); i++) {
            docPartNews.get(i).setDocNameAttachementInfo(docPartOlds.get(i).getDocNameAttachementInfo());
            docPartNews.get(i).setDocNameDocument(docPartOlds.get(i).getDocNameDocument());
            docPartNews.get(i).setDocNameFileAddPaths(docPartOlds.get(i).getDocNameFileAddPaths());
            docPartNews.get(i).setDocNameFileDeleteIds(docPartOlds.get(i).getDocNameFileDeleteIds());
            docPartNews.get(i).setDocNameMultiFileIds(docPartOlds.get(i).getDocNameMultiFileIds());
            docPartNews.get(i).setDocNameMultiFileNames(docPartOlds.get(i).getDocNameMultiFileNames());
            docPartDao.save(docPartNews.get(i));

            //查询设备文档记录是否存在附件
            String documents = "select ID from BASE_DOCUMENT where MAIN_MODEL_ID = " + beamID.toString() + " and LINK_ID = " + docPartOlds.get(i).getId().toString() + " and VALID = 1";
            List<Object> documentLists = docPartDao.createNativeQuery(documents).list();
            if (documentLists.size() > 0) {
                //取base_document新的ID
                Long documentMax = idGenerator.getNextId("BASE_DOCUMENT");
                String copyDocumentSql = "insert into BASE_DOCUMENT (ID,MAIN_MODEL_ID,LINK_ID,VERSION," + documentCol + ") select " + documentMax.toString() + "," + eamIDMax.toString() + "," + docPartNews.get(i).getId().toString() + ",1," + documentCol
                        + " from BASE_DOCUMENT where MAIN_MODEL_ID = " + beamID.toString() + " and LINK_ID = " + docPartOlds.get(i).getId().toString() + " and VALID = 1 and type = 'BEAM_baseInfo_docPart' and property_code = 'BEAM_1.0.0_baseInfo_DocPart_docName'";
                baseInfoDao.createNativeQuery(copyDocumentSql).executeUpdate();
                baseInfoDao.flush();
                baseInfoDao.clear();
            }
        }

        //设备档案附件复制
        String attchs = "select ID from BASE_DOCUMENT where MAIN_MODEL_ID = " + beamID.toString() + " and LINK_ID = " + beamID.toString() + " and VALID = 1 and type = 'BEAM_baseInfo_baseInfo' and (property_code = 'BEAM_1.0.0_baseInfo_BaseInfo_eamPic' or property_code is null)";
        List<Object> documentLists = docPartDao.createNativeQuery(attchs).list();
        if (documentLists.size() > 0) {
            for (Object documentList:documentLists) {
                //取base_document新的ID
                Long attchMax = idGenerator.getNextId("BASE_DOCUMENT");
                String copyAttchMaxSql = "insert into BASE_DOCUMENT (ID,MAIN_MODEL_ID,LINK_ID,VERSION," + documentCol + ") select " + attchMax.toString() + "," + eamIDMax.toString() + "," + eamIDMax.toString() + ",1," + documentCol
                        + " from BASE_DOCUMENT where ID = " + documentList.toString() + " and VALID = 1";
                baseInfoDao.createNativeQuery(copyAttchMaxSql).executeUpdate();
                baseInfoDao.flush();
                baseInfoDao.clear();
            }
        }

        //5.技术参数复制，因为他的设备ID为EAM_ID,和其他的不一样
        String baseinfoParamCol = getCols("BaseinfoParam");
        String baseinfoParamSql = "select ID from BEAM_BASEINFO_PARAMS where EAM_ID = " + beamID + " and VALID = 1";
        Query baseinfoParamQuery = sparePartDao.createNativeQuery(baseinfoParamSql);
        List<BigDecimal> baseinfoParamIDs = baseinfoParamQuery.list();
        for (BigDecimal baseinfoParamID : baseinfoParamIDs) {
            //取新的技术参数ID
            Long baseinfoParamMax = idGenerator.getNextId("BEAM_BASEINFO_PARAMS");
            //复制技术参数
            String copybaseinfoParamSql = "insert into BEAM_BASEINFO_PARAMS (ID,EAM_ID,CID,VERSION," + baseinfoParamCol + ") select " + baseinfoParamMax.toString() + "," + eamIDMax.toString() + ","+ CID.toString() + ",1," + baseinfoParamCol
                    + " from BEAM_BASEINFO_PARAMS where ID = " + baseinfoParamID.toString() + " and VALID = 1";
            baseinfoParamDao.createNativeQuery(copybaseinfoParamSql).executeUpdate();

        }

        //6.检查业务复制,检查项目复制，检查业务与检查项目为一对多的关系
        String baseCheckCol = getCols("BaseCheck");
        String baseCheckItemCol = getCols("BaseCheckItem");
        //insertTab("BEAM_BASE_CHECKS",baseCheckCol,beamID,eamIDMax);
        String baseCheckSql = "select ID from BEAM_BASE_CHECKS where EAMID = " + beamID + " and VALID = 1";
        Query baseCheckQuery = baseCheckDao.createNativeQuery(baseCheckSql);
        List<BigDecimal> baseCheckIDs = baseCheckQuery.list();
        for (BigDecimal baseCheckID : baseCheckIDs) {
            //取新的检查业务ID
            Long baseCheckMax = idGenerator.getNextId("BEAM_BASE_CHECKS");
            //复制检查业务
            String copyBaseCheckSql = "insert into BEAM_BASE_CHECKS (ID,EAMID,CID,VERSION," + baseCheckCol + ") select " + baseCheckMax.toString() + "," + eamIDMax.toString() + ","+ CID.toString() + ",1," + baseCheckCol
                    + " from BEAM_BASE_CHECKS where ID = " + baseCheckID.toString() + " and VALID = 1";
            baseCheckDao.createNativeQuery(copyBaseCheckSql).executeUpdate();
            //检查项目,一对多
            String baseCheckItemSql = "select ID from BEAM_BASE_CHECK_ITEMS where BASE_CHECK = " + baseCheckID.toString() + " and VALID = 1";
            Query baseCheckItemQuery = baseCheckDao.createNativeQuery(baseCheckItemSql);
            List<BigDecimal> baseCheckItemIDs = baseCheckItemQuery.list();
            for (BigDecimal baseCheckItemID : baseCheckItemIDs) {
                //取新的检查项目ID
                Long baseCheckItemMax = idGenerator.getNextId("BEAM_BASE_CHECK_ITEMS");
                //复制检查项目
                String copyBaseCheckItemSql = "insert into BEAM_BASE_CHECK_ITEMS (ID,EAMID,BASE_CHECK,CID,VERSION," + baseCheckItemCol + ") select " + baseCheckItemMax.toString() + "," + eamIDMax.toString() + "," + baseCheckMax.toString() +","+ CID.toString() + ",1," + baseCheckItemCol
                        + " from BEAM_BASE_CHECK_ITEMS where ID = " + baseCheckItemID.toString() + " and VALID = 1";
                baseCheckItemDao.createNativeQuery(copyBaseCheckItemSql).executeUpdate();
            }
        }

        //7.检维修项目复制
        String JWXItemCol = getCols("JWXItem");
        insertTab("BEAM_JWXITEMS",JWXItemCol,beamID,eamIDMax);

        //8.处理记录添加复制信息
        BEAMDealInfoTable dealInfoTable = new BEAMDealInfoTable();//创建新的处理记录
        dealInfoTable.setEamID(baseInfoNew);//设备档案
        dealInfoTable.setDealTime(new Date());//处理时间
        Staff dealStaff=new Staff();
        dealStaff.setId(((User)getCurrentUser()).getStaff().getId());
        dealInfoTable.setDealStaffID(dealStaff);//处理人
        dealInfoTable.setContent("复制设备档案");//备注信息
        dealInfoTableService.saveDealInfoTable(dealInfoTable, null);

        //将复制后的设备对象在助记码表中新增一条对应记录，否则使用助记码查找设备时，无法查找到该条数据
        Long maxId = idGenerator.getNextId("EAM_BASEINFO_MC");
        String sql = "INSERT INTO EAM_BASEINFO_MC (ID,VERSION,MNE_CODE,BASE_INFO) VALUES (?,?,?,?)";
        baseInfoDao.createNativeQuery(sql,maxId,0,beamCode,eamIDMax).executeUpdate();

        return eamIDMax;
    }
    /**
     * @Description 获得表字段
     * @param tableCode 表的编码
     * @return 表字段，用“,”隔开
     **/
    public String getCols(String tableCode){
        //获得表字段
        String beamColSql = "select column_name from ec_property where code like 'BEAM\\_1.0.0\\_baseInfo\\_" + tableCode + "\\_%' escape '\\' and valid = 1";
        Query beamColQuery =  baseInfoDao.createNativeQuery(beamColSql);
        List<String> beamCols = beamColQuery.list();
        String baseInfoCol = "";
        for (String beamCol : beamCols) {
            //设备档案表，去掉EAM_ID,EAM_CODE,EAM_NAME,IS_COPY,OA字段--EXTRA_COL，表里面没有
            if(tableCode == "BaseInfo"){
                if(!beamCol.equals("EAM_ID") && !beamCol.equals("EAM_CODE") && !beamCol.equals("EAM_NAME") && !beamCol.equals("IS_COPY") && !beamCol.equals("EAMID") && !beamCol.equals("VERSION") && !beamCol.equals("EXTRA_COL")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //1、附属设备，去掉ID，EAMID,STATUS字段数据库不存在
            if(tableCode == "AttachPart"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //2、备件清单，去掉ID，EAMID,STATUS字段数据库不存在
            if(tableCode == "SparePart"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //3、设备文档，去掉ID，EAMID,STATUS字段数据库不存在
            if(tableCode == "DocPart"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("STATUS") && !beamCol.equals("DOC_NAME") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //5、技术参数，去掉ID，EAM_ID,STATUS字段数据库不存在
            if(tableCode == "BaseinfoParam"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAM_ID") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //6、检查业务，去掉ID，EAMID,STATUS字段数据库不存在
            if(tableCode == "BaseCheck"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //检查项目，去掉ID，EAMID，BASE_CHECK,STATUS字段数据库不存在
            if(tableCode == "BaseCheckItem"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("BASE_CHECK") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION")){
                    baseInfoCol += beamCol + ",";
                }
            }
            //7、检维修项目，去掉ID，EAMID,STATUS字段数据库不存在
            if(tableCode == "JWXItem"){
                if(!beamCol.equals("ID") && !beamCol.equals("EAMID") && !beamCol.equals("STATUS") && !beamCol.equals("VERSION") && !beamCol.equals("IS_TO_CHECK")){
                    baseInfoCol += beamCol + ",";
                }
            }
        }
        if (baseInfoCol != "") {
            baseInfoCol = baseInfoCol.substring(0, baseInfoCol.length() - 1);
        }
        return baseInfoCol;
    }
    /**
     * @Description 页签内容复制
     * @param tableName 表名
     * @param beamID 被复制的设备ID
     * @param eamIDMax 复制后的设备ID
     * @return 空
     **/
    public void insertTab(String tableName, String tableCols, Long beamID, Long eamIDMax){
        String Sql = "select ID from " + tableName + " where EAMID = " + beamID.toString() + " and VALID = 1";
        Query Query = baseInfoDao.createNativeQuery(Sql);
        List<BigDecimal> partIDs = Query.list();
        //取当前公司ID
        Long CID = getCurrentCompanyId();
        for (BigDecimal partID : partIDs) {
            //取新的ID
            Long partMax = idGenerator.getNextId(tableName);
            //复制附属设备
            String copyPartSql = "insert into " + tableName + " (ID,EAMID,CID,VERSION," + tableCols + ") select " + partMax.toString() + "," + eamIDMax.toString() + ","+ CID.toString() + ",1," + tableCols
                    + " from " + tableName + " where ID = " + partID.toString() + " and VALID = 1";
            baseInfoDao.createNativeQuery(copyPartSql).executeUpdate();
        }
    }

    /**
     * 判断模块是否已上载、发布
     * @param modelCode 如'BEAM_1.0.0'
     * @param symbolicName ：com.supcon.orchid.BEAM.service
     * return 0:未上载；1：已发布；2：已上载，但是不存在发布记录；3：未启动
     */
    @Override
    @Transactional
    public String findModel(String modelCode, String symbolicName){
        String modelSql = "SELECT ARTIFACT FROM EC_MODULE WHERE CODE = ? AND VALID = 1";
        List<Object> results = baseInfoDao.createNativeQuery(modelSql,modelCode).list();
        if(results == null || results.size() == 0){
            //模块未上载
            return "0";
        }
        String deploySql = "SELECT BUNDLE_SYMBOLICNAME FROM SYS_DEPLOYMENT_LOG WHERE BUNDLE_SYMBOLICNAME = ?";
        List<Object> deployResults = baseInfoDao.createNativeQuery(deploySql,symbolicName).list();
        if(deployResults == null || deployResults.size() == 0){
            //已上载，但模块不存在发布记录
            return "2";
        }
        Bundle bundle = FrameworkUtil.getBundle(getClass());
        Bundle[] allBundles = bundle.getBundleContext().getBundles();
        //String symbolicName = "com.supcon.orchid.BEAM.service";
        for (Bundle bdl : allBundles) {
            if (symbolicName.equals(bdl.getSymbolicName())) {
                //模块已发布
                return "1";
            }
        }
        //设备模块未发布
        return "3";
    }

    /**
	 * 根据业务ID判断是否存在工单记录 by jiangshiyi
	 * @param jwxItemID
	 * @param sourceType
	 * @return
	 */
	@Override
	public boolean isHasWorkRecord(String jwxItemID, String sourceType){
		// 将jwxItemID变为数组
		if (StringUtils.isEmpty(jwxItemID)){
			return false;
		}
		List<Long> jwxItemIDLists = getPartIDs(jwxItemID);
		boolean flag = false;
		// 来源润滑BEAM2003/02维保BEAM2003/03，直接查询工单
		if ("BEAM2003/02".equals(sourceType) || "BEAM2003/03".equals(sourceType)) {
			String isWorkSql = "SELECT ID FROM BEAM2_WORK_RECORDS WHERE JWX_ITEM IN (:params) AND VALID = 1 AND STATUS != 0";
			flag = isHasRecord(isWorkSql,jwxItemIDLists);
		}
		// 来源备件BEAM2003/04，查询备件记录pt
		if ("BEAM2003/04".equals(sourceType)) {
			String isWorkSql = "SELECT THIS_.ID FROM BEAM2_SPARE_PARTS THIS_ LEFT JOIN BEAM2_WORK_RECORDS WORKRECORD ON THIS_.WORK_LIST = WORKRECORD.ID WHERE THIS_.SPARE_PART_ID IN (:params) AND THIS_.VALID = 1 AND WORKRECORD.STATUS != 0";
			flag = isHasRecord(isWorkSql,jwxItemIDLists);
		}
		return flag;
	}

	/**
	 * 根据业务ID判断是否存在检定计划或者检定记录 by jiangshiyi
	 * @param baseCheckID
	 * @param sourceType
	 * @return
	 */
	@Override
	public boolean isHasBaseChekRecord(String baseCheckID, String sourceType){
		if (StringUtils.isEmpty(baseCheckID) || StringUtils.isEmpty(sourceType)) {
			return false;
		}
		List<Long> baseCheckIDLists = getPartIDs(baseCheckID);
		boolean flag = false;
		// 测量设备
		if ("isMea".equals(sourceType)) {
			// 首先查询测量模块是否上载
			String result = findModel("MSBEAM_1.0.0","com.supcon.orchid.MSBEAM.service");
			if ("0".equals(result)) {
				// 模块未上载
				return false;
			}
			// 查询是否已制定计划
			String getMeaPlan = "SELECT PLANPART.ID FROM MSBEAM_MEA_PLAN_PARTS PLANPART LEFT JOIN MSBEAM_MEA_PLANS PLANS ON PLANPART.HEAD_ID = PLANS.ID WHERE PLANPART.BASECHECKID IN (:params) AND PLANPART.VALID = 1 AND PLANS.VALID = 1 AND PLANS.STATUS != 0";
			flag = isHasRecord(getMeaPlan,baseCheckIDLists);
			// 如果未制定计划，查询是否已生成检定记录
			if (!flag) {
				String getMeaRecord = "SELECT PART.ID FROM MSBEAM_CHECK_INFO_DETAILS PART LEFT JOIN MSBEAM_CHECK_INFOS HEAD ON PART.HEADID = HEAD.ID WHERE PART.BASE_CHECKID IN (:params) AND PART.VALID = 1 AND HEAD.VALID = 1 AND HEAD.STATUS != 0";
				flag = isHasRecord(getMeaRecord,baseCheckIDLists);
			}
		}
		// 特种设备
		if ("isSpecial".equals(sourceType)) {
			// 首先查询测量模块是否上载
			String result = findModel("SBEAM_1.0.0","com.supcon.orchid.SBEAM.service");
			if ("0".equals(result)) {
				// 模块未上载
				return false;
			}
			// 查询是否已制定计划
			String getSpecialPlan = "SELECT PLANPART.ID FROM SBEAM_SPECIAL_PLAN_PARTS PLANPART LEFT JOIN SBEAM_SPECIAL_PLANS PLANS ON PLANPART.HEADID = PLANS.ID WHERE PLANPART.BASECHECKID IN (:params) AND PLANPART.VALID = 1 AND PLANS.VALID = 1 AND PLANS.STATUS != 0";
			flag = isHasRecord(getSpecialPlan,baseCheckIDLists);
			// 如果未制定计划，查询是否已生成检定记录
			if (!flag) {
				String getSpecialRecord = "SELECT PART.ID FROM SBEAM_CHECK_INFO_DETAILS PART LEFT JOIN MSBEAM_CHECK_INFOS HEAD ON PART.HEADID = HEAD.ID WHERE PART.BASE_CHECKID IN (:params) AND PART.VALID = 1 AND HEAD.VALID = 1 AND HEAD.STATUS != 0";
				flag = isHasRecord(getSpecialRecord,baseCheckIDLists);
			}
		}
		return flag;
	}

    /**
	 * 根据业务ID判断是否存在检定计划或者检定记录,返回存在记录的检查业务ID
	 * @param baseCheckID
	 * @return
	 */
	@Override
	public String hasRecordIds(String baseCheckID){
		String results = "";
		if (StringUtils.isEmpty(baseCheckID)) {
			return results;
		}
		List<Long> baseCheckIDLists = getPartIDs(baseCheckID);
		// 首先查询测量模块是否上载
		String meaResult = findModel("MSBEAM_1.0.0","com.supcon.orchid.MSBEAM.service");
		// 首先查询测量模块是否上载
		String spResult = findModel("SBEAM_1.0.0","com.supcon.orchid.SBEAM.service");
		if ("0".equals(meaResult) && "0".equals(spResult)) {
			// 模块未上载
			return results;
		}
		String getRecordsSql = "";
		// 查询测量检查业务是否已生成计划或者检定记录
		if (!"0".equals(meaResult)) {
			// 查询是否已制定计划
			getRecordsSql = "SELECT PLANPART.BASECHECKID FROM MSBEAM_MEA_PLAN_PARTS PLANPART LEFT JOIN MSBEAM_MEA_PLANS PLANS ON PLANPART.HEAD_ID = PLANS.ID WHERE PLANPART.BASECHECKID IN (:params) AND PLANPART.VALID = 1 AND PLANS.VALID = 1 AND PLANS.STATUS != 0";
			// 查询是否已生成检定记录
			getRecordsSql += " UNION SELECT PART.BASE_CHECKID FROM MSBEAM_CHECK_INFO_DETAILS PART LEFT JOIN MSBEAM_CHECK_INFOS HEAD ON PART.HEADID = HEAD.ID WHERE PART.BASE_CHECKID IN (:params) AND PART.VALID = 1 AND HEAD.VALID = 1 AND HEAD.STATUS != 0";
		}
		if (!"0".equals(spResult)) {
			if (!StringUtils.isEmpty(getRecordsSql)) {
				getRecordsSql += " UNION ";
			}
			// 查询是否已制定计划
			getRecordsSql += "SELECT PLANPART.BASECHECKID FROM SBEAM_SPECIAL_PLAN_PARTS PLANPART LEFT JOIN SBEAM_SPECIAL_PLANS PLANS ON PLANPART.HEADID = PLANS.ID WHERE PLANPART.BASECHECKID IN (:params) AND PLANPART.VALID = 1 AND PLANS.VALID = 1 AND PLANS.STATUS != 0";
			// 查询是否已生成检定记录
			getRecordsSql += " UNION SELECT PART.BASE_CHECKID FROM SBEAM_CHECK_INFO_DETAILS PART LEFT JOIN MSBEAM_CHECK_INFOS HEAD ON PART.HEADID = HEAD.ID WHERE PART.BASE_CHECKID IN (:params) AND PART.VALID = 1 AND HEAD.VALID = 1 AND HEAD.STATUS != 0";
		}
		if (!StringUtils.isEmpty(getRecordsSql)) {
			results += getBaseCheckIds(getRecordsSql,baseCheckIDLists);
		}
		return results;
	}

	/**
	 * 根据传入的以逗号分割的ID字符串，返回一个list数组 by jiangshiyi
	 * @param partIDs
	 * @return
	 */
	public List<Long> getPartIDs(String partIDs){
		List<Long> partIDLists = new ArrayList<Long>();
		if (StringUtils.isEmpty(partIDs)) {
			return partIDLists;
		}
		String[] partIDss = partIDs.split(",");
		for (String partIDsStr:partIDss) {
			if (!StringUtils.isEmpty(partIDsStr)) {
				partIDLists.add(Long.valueOf(partIDsStr));
			}
		}
		return partIDLists;
	}
	/**
	 * 查询是否存在记录  by jiangshiyi
	 * @param sql
	 * @param params
	 * @return
	 */
	public boolean isHasRecord(String sql, List<Long> params){
		Query query = baseInfoDao.createNativeQuery(sql);
		query.setParameterList("params",params);
		List<Object> results = query.list();
		if (results == null || results.size() == 0) {
			return false;
		}
		return true;
	}

    /**
	 * 查询存在记录的业务ID  by jiangshiyi
	 * @param sql
	 * @param params
	 * @return
	 */
	public String getBaseCheckIds(String sql, List<Long> params){
		String baseCheckIds = "";
		Query query = baseInfoDao.createNativeQuery(sql);
		query.setParameterList("params",params);
		List<Object> results = query.list();
		if (results == null || results.size() == 0) {
			return baseCheckIds;
		}
		for (Object result : results) {
			if (result != null && !StringUtils.isEmpty(result.toString())) {
				baseCheckIds += result.toString() + ",";
			}
		}
		return baseCheckIds;
	}

	/**
	 * 根据设备id获得设备其他相关信息，如运行时长等
	 * @param eamID
	 * @return
	 */
	@Override
	public Map<String, Map> getEamOtherInfo(String eamID){
		Map<String, Map> result = new HashMap<>();
		// 根据设备信息获得运行数据
		Map<String, String> runResult = getRunInfo(eamID);
		result.put("runInfos",runResult);
      
      	// 根据设备ID获得上次点检时间、下次点检时间
		Map<String, String> pointResult = getPointInfos(eamID);
		result.put("pointInfos",pointResult);
      	//业务性质(润滑)
		String busiType="BEAM010/03";
		//根据设备信息获取上次润滑时间和下次润滑时间
		Map<String, String> lubricateResult = getLubricateInfo(eamID,busiType);
		result.put("lubriInfos",lubricateResult);
		//业务性质(润滑)
		busiType="BEAM010/04";
		//根据设备信息获取上次维保时间和下次维保时间
		Map<String, String> repairResult = getLubricateInfo(eamID,busiType);
		result.put("repairInfos",repairResult);
		//获取上次检修时间和检修内容
		Map<String, String> checkResult = getcheckInfo(eamID);
		result.put("checkInfos",checkResult);
		return result;
	}
	/**
	 * 根据设备信息获得上次检修内容和上次检修时间
	 * @param eamID
	 * @return
	 */
	private Map<String, String> getcheckInfo(String eamID) {
		Map<String, String> checkResult = new HashMap<>();
		String sql="SELECT REMARK, MAX(REAL_END_DATE) FROM BEAM2_WORK_RECORDS WHERE WORK_SOURCE='BEAM2003/01' AND STATUS=99 AND VALID=1 AND EAMID=? GROUP BY REMARK ";
		List<Object[]> checkInfos = baseInfoDao.createNativeQuery(sql,eamID).list();
		if (checkInfos == null || checkInfos.size() == 0) {
			return  checkResult;
		}
		Object[] checkObj = checkInfos.get(0);
		checkResult.put("checkTime",checkObj[1] == null?"":checkObj[1].toString());
		checkResult.put("checkContent",checkObj[0] == null?"":checkObj[0].toString());
		return checkResult;
	}
	/**
	 * 根据设备信息获得上次润滑时间和下次润滑时间
	 * @param eamID
	 * @return
	 */
	private Map<String, String> getLubricateInfo(String eamID,String busiType) {
		Map<String, String> runResult = new HashMap<>();
		//查询业务规格的最大的上次执行时间和最小的下次执行时间
		String sql="select max(LAST_TIME),min(NEXT_TIME) from BEAM_JWXITEMS where valid=1 and BUSI_PROPERTY=? and EAMID=? ";
		List<Object[]> lubriInfos = baseInfoDao.createNativeQuery(sql,busiType,eamID).list();
		if (lubriInfos == null || lubriInfos.size() == 0) {
			return  runResult;
		}
		Object[] lubriObj = lubriInfos.get(0);
		runResult.put("lastTime",lubriObj[0] == null?"":lubriObj[0].toString());
		runResult.put("nextTime",lubriObj[1] == null?"":lubriObj[1].toString());
		return runResult;
	}

	/**
	 * 根据设备信息获得运行数据
	 * @param eamID
	 * @return
	 */
	public Map<String, String> getRunInfo(String eamID) {
		Map<String, String> runResult = new HashMap<>();
		// 获得运行信息
		String getRunInfoSql = "SELECT MAX(RunningGathers.RUNNING_PARAM) AS id, SUM(RunningGathers.RUN_TOTAL_HOUR) + MAX( MeasParam.INITIAL_VALUE ) AS runTotal, SUM(RunningGathers.CLOSE_TOTAL_HOUR) AS stopTotal, SUM(RunningGathers.CLOSE_SUM) AS stopNum, MAX(MeasParam.INITIAL_VALUE) AS initValue, MAX(MeasParam.LATEST_VALUE) AS lastValue, MAX(MeasParam.LATEST_CREATETIME) AS lastTime FROM BEAM2_RUNNING_GATHERS RunningGathers LEFT JOIN BEAM_MEAS_PARAMS MeasParam ON MeasParam.ID = RunningGathers.RUNNING_PARAM  WHERE RunningGathers.EAMID = ? AND RunningGathers.VALID = 1 AND MeasParam.VALID = 1";
		List<Object[]> runInfos = baseInfoDao.createNativeQuery(getRunInfoSql,eamID).list();
		if (runInfos == null || runInfos.size() == 0) {
			return  runResult;
		}
		Object[] runObj = runInfos.get(0);
		// 运行参数ID
		runResult.put("id",runObj[0] == null?"":runObj[0].toString());
		BigDecimal runTotal = runObj[1] == null?null:new BigDecimal(runObj[1].toString());
		BigDecimal stopTotal = runObj[2] == null?null:new BigDecimal(runObj[2].toString());
		BigDecimal stopNum = runObj[3] == null?null:new BigDecimal(runObj[3].toString());
		// 最后一次开机状态以及开始时间不为空，则计算最后一次持续时长
		if (runObj[5] != null && runObj[6] != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date endDate = new Date();
			Date startDate = null;
			BigDecimal lastTotal = null;
			try {
				startDate = sdf.parse(runObj[6].toString());
			} catch (ParseException e) {
				log.info(e.getMessage());
				throw new BAPException("开始时间格式转换失败！");
			}
			if (startDate.getTime() < endDate.getTime()) {
				//格式化小数(保留6位小数)
				DecimalFormat df = new DecimalFormat("0.000000");
				// 小时
				double diffHours = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000.00);
				String num = df.format(diffHours);
				lastTotal = new BigDecimal(num);
			}
          	// "1".equals(runObj[5].toString())
			if (Double.valueOf(runObj[5].toString()).intValue() == 1  && lastTotal != null) {
				runTotal = runTotal == null?lastTotal:runTotal.add(lastTotal);
			}
          	// "0".equals(runObj[5].toString())
			if (Double.valueOf(runObj[5].toString()).intValue() == 0) {
				stopTotal = stopTotal == null?lastTotal:stopTotal.add(lastTotal);
			}
		}
      	BigDecimal runDay = new BigDecimal(0);
		BigDecimal runHH = new BigDecimal(0);
		BigDecimal runMM = new BigDecimal(0);
		BigDecimal runSS = new BigDecimal(0);
		BigDecimal stopDay = new BigDecimal(0);
		BigDecimal stopHH = new BigDecimal(0);
		BigDecimal stopMM = new BigDecimal(0);
		BigDecimal stopSS = new BigDecimal(0);
      	if (runTotal != null) {
			BigDecimal runDivide[] = runTotal.divideAndRemainder(new BigDecimal(24));
			// 天
			runDay = runDivide[0].setScale(0, BigDecimal.ROUND_DOWN);
			// 时
			runHH = runDivide[1].setScale(0, BigDecimal.ROUND_DOWN);
			// 分
			BigDecimal runMMSS = (runDivide[1].subtract(runHH)).multiply(new BigDecimal(60));
			runMM = runMMSS.setScale(0, BigDecimal.ROUND_DOWN);
			// 秒
			runSS = (runMMSS.subtract(runMM)).multiply(new BigDecimal(60)).setScale(0, BigDecimal.ROUND_HALF_UP);
		}
		if (stopTotal != null) {
			BigDecimal stopDivide[] = stopTotal.divideAndRemainder(new BigDecimal(24));
			// 天
			stopDay = stopDivide[0].setScale(0, BigDecimal.ROUND_DOWN);
			// 时
			stopHH = stopDivide[1].setScale(0, BigDecimal.ROUND_DOWN);
			// 分
			BigDecimal stopMMSS = (stopDivide[1].subtract(stopHH)).multiply(new BigDecimal(60));
			stopMM = stopMMSS.setScale(0, BigDecimal.ROUND_DOWN);
			// 秒
			stopSS = (stopMMSS.subtract(stopMM)).multiply(new BigDecimal(60)).setScale(0, BigDecimal.ROUND_HALF_UP);
		}
		runResult.put("runTotal",runTotal == null?"":runTotal.toString());
		runResult.put("runDay",runDay == null?"":runDay.toString());
		runResult.put("runHH",runHH == null?"":runHH.toString());
		runResult.put("runMM",runMM == null?"":runMM.toString());
		runResult.put("runSS",runSS == null?"":runSS.toString());
		runResult.put("stopTotal",stopTotal == null?"":stopTotal.toString());
		runResult.put("stopDay",stopDay == null?"":stopDay.toString());
		runResult.put("stopHH",stopHH == null?"":stopHH.toString());
		runResult.put("stopMM",stopMM == null?"":stopMM.toString());
		runResult.put("stopSS",stopSS == null?"":stopSS.toString());
		runResult.put("stopNum",stopNum == null?"":stopNum.toString());
      	runResult.put("lastValue",runObj[5] == null?"":runObj[5].toString());
		return  runResult;
	}

	/**
	 * 根据设备信息获得点检数据
	 * @param eamID
	 * @return
	 */
	public Map<String, String> getPointInfos(String eamID) {
		Map<String, String> pointResult = new HashMap<>();
		// 判断巡检模块是否已经上载
		String isExtra = findModel("mobileEAM_1.0.0","com.supcon.orchid.mobileEAM.service");
		if ("0".equals(isExtra)) {
			return pointResult;
		}
		// 上次执行时间
		String lastTimeSql = "select MAX(b.END_TIME_ACTUAL) AS lastTime from MOBILEEAM_POTROLTPARTWFS a left join MOBILEEAM_POTROL_TASKWFS b on a.TASKID = b.id where a.EAMID = ? AND b.STATUS = 99 AND a.VALID = 1 AND b.VALID = 1";
		List<Object> lastTimeObj = baseInfoDao.createNativeQuery(lastTimeSql,eamID).list();
		String lastTime = "";
		if (lastTimeObj != null && lastTimeObj.size() > 0) {
			lastTime = lastTimeObj.get(0) == null?"":lastTimeObj.get(0).toString();
		}
		pointResult.put("lastTime",lastTime);

		// 下次执行时间
		String nextTimeSql = "SELECT MIN(b.STAR_TIME) AS newxTime from MOBILEEAM_POTROLTPARTWFS a left join MOBILEEAM_POTROL_TASKWFS b on a.TASKID = b.id where a.EAMID = ? AND b.STATUS = 99 AND b.LINK_STATE in ('LinkState/01','LinkState/02') ";
		if (!StringUtils.isEmpty(lastTime)) {
			nextTimeSql += " AND b.STAR_TIME >= ? ";
		}
		nextTimeSql += " AND a.VALID = 1 AND b.VALID = 1";
		String nextTime = "";
		List<Object> nextTimeObj = new ArrayList<Object>();
		if (!StringUtils.isEmpty(lastTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(lastTime);
			} catch (ParseException e) {
				log.info(e.getMessage());
				throw new BAPException("开始时间格式转换失败！");
			}
			nextTimeObj = baseInfoDao.createNativeQuery(nextTimeSql,eamID,date).list();
		} else {
			nextTimeObj = baseInfoDao.createNativeQuery(nextTimeSql,eamID).list();
		}

		if (nextTimeObj != null && nextTimeObj.size() > 0) {
			nextTime = nextTimeObj.get(0) == null?"":nextTimeObj.get(0).toString();
		}
		pointResult.put("nextTime",nextTime);

		return pointResult;
	}

	@Autowired
	private BEAMChangeInfoDao changeInfoDao;
	/**
	 * 根据设备id获得变更信息
	 * @param changePage
	 * @param beamID
	 * @return
	 */
	@Override
	public Page<BEAMChangeInfo> getChangeInfo(Page<BEAMChangeInfo> changePage, Long beamID){
		BEAMBaseInfo baseInfo = baseInfoDao.load(Long.valueOf(beamID));
		String sql = "select T.id AS \"id\", T.EAMID AS \"eamID.id\", T.EFFECT_TIME AS \"changeTime\", T.changeType AS \"changeType\", T.TABLE_NO AS \"tableNo\", T.changeInfo AS \"changeInfo\",T.changeStaffID AS \"changeStaffID\",staff.name,systemCode.value from (select id,EAMID,EFFECT_TIME,cast('BEAM064/01' as varchar(10)) AS changeType,TABLE_NO,REASON AS changeInfo, CREATE_STAFF_ID AS changeStaffID from BEAM2_WORK_ALLOTS where eamID = ? and status = 99 and valid = 1 union all select a.id,a.EAMID,b.EFFECT_TIME,cast('BEAM064/02' as varchar(10)) AS changeType,b.TABLE_NO,a.CHANGE_REASON AS changeInfo,b.CHANGE_STAFF AS changeStaffID from BEAM_CHANGE_PARTS a LEFT JOIN BEAM_CHANGES b on a.changeid = b.id where a.eamID = ? and b.status = 99 and a.valid = 1 and b.valid = 1) T LEFT JOIN BASE_STAFF staff ON changeStaffID = staff.ID LEFT JOIN BASE_SYSTEMCODE systemCode ON systemCode.ID = T.changeType ORDER BY EFFECT_TIME DESC";
		SQLQuery query = changeInfoDao.createNativeQuery(sql,beamID,beamID);
		List<Object[]> listObjs = query.list();
		List<BEAMChangeInfo> listResults = new ArrayList<BEAMChangeInfo>();
		for (Object[] listObj : listObjs) {
			BEAMChangeInfo changeInfo = new BEAMChangeInfo();
			if (listObj[0] != null) {
				changeInfo.setId(Long.valueOf(listObj[0].toString()));
			}
			changeInfo.setEamID(baseInfo);
			if (listObj[2] != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date date = null;
				try {
					date = sdf.parse(listObj[2].toString());
				} catch (ParseException e) {
					log.info(e.getMessage());
					throw new BAPException("开始时间格式转换失败！");
				}
				changeInfo.setChangeTime(date);
			}			
			SystemCode changeType = new SystemCode(listObj[3].toString());
			changeType.setValue(listObj[8] == null?"":listObj[8].toString());
			changeInfo.setChangeType(changeType);
			changeInfo.setTableNo(listObj[4] == null?"":listObj[4].toString());
			changeInfo.setChangeInfo(listObj[5] == null?"":listObj[5].toString());
			// 变更人
			if (listObj[6] != null) {
				Staff staff = new Staff();
				staff.setId(Long.valueOf(listObj[6].toString()));
				staff.setName(listObj[7] == null?"":listObj[7].toString());
				changeInfo.setChangeStaff(staff);
			}
          
			listResults.add(changeInfo);
		}

		changePage.setResult(listResults);

		String countSql = "SELECT COUNT(*) FROM (" + sql + ") T";
		List<Object> totalCount = changeInfoDao.createNativeQuery(countSql,beamID,beamID).list();
		changePage.setTotalCount(Integer.valueOf(totalCount.get(0).toString()));

		return changePage;
	}

	/**
	 * 根据设备id获得设备图片
	 * @param eamID
	 * @return
	 */
	@Override
	public String getPicDocIDS(String eamID){
		if (StringUtils.isEmpty(eamID)) {
			return "";
		}
		String resultIDs = "";
		// 设备主图片
		String getMainPicSql = "SELECT ID FROM BASE_DOCUMENT WHERE MAIN_MODEL_ID = ? AND LINK_ID = ? AND TYPE = 'BEAM_baseInfo_baseInfo' AND PROPERTY_CODE = 'BEAM_1.0.0_baseInfo_BaseInfo_eamPic' AND VALID = 1";
		List<Object> mainPicObjs = baseInfoDao.createNativeQuery(getMainPicSql,eamID,eamID).list();
		if (mainPicObjs != null && mainPicObjs.size() > 0 && mainPicObjs.get(0) != null) {
			resultIDs = mainPicObjs.get(0).toString() + ",";
		}

		// 设备文档图片
		String getSparePartPicSql = "SELECT ID,PATH FROM BASE_DOCUMENT WHERE MAIN_MODEL_ID = ? AND LINK_ID IN (SELECT ID FROM BEAM_DOC_PARTS WHERE EAMID = ? AND VALID = 1) AND TYPE = 'BEAM_baseInfo_docPart' AND PROPERTY_CODE = 'BEAM_1.0.0_baseInfo_DocPart_docName' AND VALID = 1";
		List<Object[]> spareParticObjs = baseInfoDao.createNativeQuery(getSparePartPicSql,eamID,eamID).list();
		if (spareParticObjs == null || spareParticObjs.size() == 0) {
			return resultIDs;
		}
		for (Object[] spareParticObj : spareParticObjs) {
			// 判断附件类型
			if (spareParticObj[1] != null) {
				String path = spareParticObj[1].toString();
				int i = path.lastIndexOf('.');
				String extension = "";
				if (i > 0) {
					extension = path.substring(i+1);
				}
				if("JPG".equals(extension.toUpperCase()) || "PNG".equals(extension.toUpperCase()) || "JPEG".equals(extension.toUpperCase())){
					resultIDs += spareParticObj[0].toString() + ",";
				}
			}
		}
		return resultIDs;
	}
	
	@Override
	public List<Object[]> getEamRunningInfo(String eamID) {
		if (StringUtils.isEmpty(eamID)) {
			return null;
		}
		String sql="SELECT ID,NAME,LATEST_VALUE,MAX_VALUE,MIN_VALUE,ITEM_NUMBER,PARAM_SORT FROM BEAM_MEAS_PARAMS WHERE EAMID=? AND VALUETYPE != 'BEAM053/01' AND VALID=1 ORDER BY PARAM_SORT ";
		List<Object[]> runningDate = new ArrayList<Object[]>();
		runningDate = baseInfoDao.createNativeQuery(sql,eamID).list();
		return runningDate;
	}
/* CUSTOM CODE END */
}
