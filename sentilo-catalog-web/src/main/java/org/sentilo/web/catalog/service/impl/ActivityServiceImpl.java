/*
 * Sentilo
 *  
 * Original version 1.4 Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de Barcelona.
 * Modified by Opentrends adding support for multitenant deployments and SaaS. Modifications on version 1.5 Copyright (C) 2015 Opentrends Solucions i Sistemes, S.L.
 * 
 *   
 * This program is licensed and may be used, modified and redistributed under the
 * terms  of the European Public License (EUPL), either version 1.1 or (at your 
 * option) any later version as soon as they are approved by the European 
 * Commission.
 *   
 * Alternatively, you may redistribute and/or modify this program under the terms
 * of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either  version 3 of the License, or (at your option) any later 
 * version. 
 *   
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. 
 *   
 * See the licenses for the specific language governing permissions, limitations 
 * and more details.
 *   
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along 
 * with this program; if not, you may find them at: 
 *   
 *   https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *   http://www.gnu.org/licenses/ 
 *   and 
 *   https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.web.catalog.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sentilo.common.domain.PlatformActivity;
import org.sentilo.common.domain.PlatformMetricsMessage;
import org.sentilo.web.catalog.context.TenantContextHolder;
import org.sentilo.web.catalog.domain.Activity;
import org.sentilo.web.catalog.repository.ActivityRepository;
import org.sentilo.web.catalog.search.SearchFilter;
import org.sentilo.web.catalog.search.SearchFilterResult;
import org.sentilo.web.catalog.service.ActivityService;
import org.sentilo.web.catalog.service.PlatformService;
import org.sentilo.web.catalog.utils.TenantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ActivityServiceImpl extends AbstractBaseCrudServiceImpl<Activity> implements ActivityService {

  @Autowired
  private ActivityRepository repository;

  @Autowired
  private PlatformService platformService;

  public ActivityServiceImpl() {
    super(Activity.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.sentilo.web.catalog.service.ActivityService#getLastRegisters()
   */
  public List<Activity> getLastActivityLogs() {
    final SearchFilter filter = buildFilter();
    final SearchFilterResult<Activity> result = search(filter);
    // the list contains the last 20 entries from the collection Activity
    // in desc order, i.e., the first element is the most recent
    return result.getContent();
  }

  @Scheduled(initialDelay = 30000, fixedRate = 3600000)
  public void deleteOldActivityLogs() {
    // Only stores the activity from the last 7 days
    final long tsToCompare = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
    final Criteria criteria = Criteria.where("timestamp").lt(tsToCompare);
    getMongoOps().remove(new Query(criteria), Activity.class);
  }

  /**
   * Method that does a call to the PubSub server to retrieve the current platform activity and
   * stores the result into the repository
   */
  @Scheduled(initialDelay = 10000, fixedRate = 300000)
  public void getAndStorePlatformActivity() {
    final PlatformMetricsMessage metrics = platformService.getPlatformActivity();
    final Collection<Activity> activities = new ArrayList<Activity>();
    for (final PlatformActivity platformActivity : metrics.getActivity()) {
      activities.add(new Activity(platformActivity, getPreviousActivity(platformActivity)));
    }

    if (!CollectionUtils.isEmpty(activities)) {
      insertAll(activities);
    }
  }

  private Activity getPreviousActivity(final PlatformActivity platformActivity) {
    final Pageable pageable = new PageRequest(0, 1, Direction.DESC, "timestamp");
    final SearchFilter filter = new SearchFilter(pageable);
    if (TenantContextHolder.isEnabled()) {
      filter.addAndParam("tenant", platformActivity.getTenant());
    }

    final SearchFilterResult<Activity> result = search(filter);

    return (CollectionUtils.isEmpty(result.getContent()) ? new Activity() : result.getContent().get(0));
  }

  private SearchFilter buildFilter() {
    final Pageable pageable = new PageRequest(0, 20, Direction.DESC, "timestamp");
    final SearchFilter filter = new SearchFilter(pageable);
    if (TenantContextHolder.isEnabled()) {
      filter.addAndParam("tenant", TenantUtils.getCurrentTenant());
    }

    return filter;
  }

  @Override
  public ActivityRepository getRepository() {
    return repository;
  }

  @Override
  public String getEntityId(final Activity entity) {
    return entity.getId();
  }
}
