package gov.hhs.cms.ff.fm.eps.ep.sbm.services.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.accenture.foundation.common.exception.ApplicationException;

import gov.hhs.cms.ff.fm.eps.ep.dao.SbmPolicyVersionDao;
import gov.hhs.cms.ff.fm.eps.ep.enums.EProdEnum;
import gov.hhs.cms.ff.fm.eps.ep.po.PolicyVersionPO;
import gov.hhs.cms.ff.fm.eps.ep.sbm.SBMPolicyDTO;
import gov.hhs.cms.ff.fm.eps.ep.sbm.SbmDataUtil;
import gov.hhs.cms.ff.fm.eps.ep.sbm.services.SbmPolicyMatchService;
import gov.hhs.cms.ff.fm.eps.ep.vo.PolicyVersionSearchCriteriaVO;

public class SbmPolicyMatchServiceImpl implements SbmPolicyMatchService {

	private final static Logger LOG = LoggerFactory.getLogger(SbmPolicyMatchServiceImpl.class);

	private SbmPolicyVersionDao sbmPolicyVersionDao;


	@Override
	public PolicyVersionPO findLatestPolicy(SBMPolicyDTO inboundDTO) {

		PolicyVersionPO po = null;

		PolicyVersionSearchCriteriaVO criteria = extractSearchCriteria(inboundDTO);

		LOG.debug("Performing Policy Match for: " + inboundDTO.getLogMsg());

		List<PolicyVersionPO> poList = sbmPolicyVersionDao.findLatestPolicyVersion(criteria);

		// can never return null.
		if (poList.size() == 1) {
			po = poList.get(0);

		} else if (poList.size() > 1) {
			String errMsg = EProdEnum.EPROD_13.getLogMsg() + ". "  + poList.size() + " policies found for " + criteria.getLogMessage();
			throw new ApplicationException(errMsg);
		}

		if (LOG.isDebugEnabled()) {
			if (po != null) {

				LOG.debug("Policy Match FOUND");

			} else {

				LOG.debug("NO Policy Match");
			}
		}
		return po;
	}


	/**
	 * Extracts ExchangePolicyID and StateCd search criteria from the inbound DTO.
	 * @param inboundDTO
	 * @return
	 */
	private PolicyVersionSearchCriteriaVO extractSearchCriteria(SBMPolicyDTO inboundDTO) {

		PolicyVersionSearchCriteriaVO criteria = new PolicyVersionSearchCriteriaVO();
		criteria.setExchangePolicyId(inboundDTO.getPolicy().getExchangeAssignedPolicyId());
		criteria.setSubscriberStateCd(SbmDataUtil.getStateCd(inboundDTO.getFileInfo()));
		return criteria;
	}


	/**
	 * @param sbmPolicyVersionDao the sbmPolicyVersionDao to set
	 */
	public void setSbmPolicyVersionDao(SbmPolicyVersionDao sbmPolicyVersionDao) {
		this.sbmPolicyVersionDao = sbmPolicyVersionDao;
	}




}
