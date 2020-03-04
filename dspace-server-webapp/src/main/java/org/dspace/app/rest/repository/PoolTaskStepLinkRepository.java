/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.rest.repository;

import java.sql.SQLException;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.dspace.app.rest.model.PoolTaskRest;
import org.dspace.app.rest.model.WorkflowStepRest;
import org.dspace.app.rest.projection.Projection;
import org.dspace.core.Context;
import org.dspace.xmlworkflow.factory.XmlWorkflowFactory;
import org.dspace.xmlworkflow.storedcomponents.PoolTask;
import org.dspace.xmlworkflow.storedcomponents.service.PoolTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component(PoolTaskRest.CATEGORY + "." + PoolTaskRest.NAME + "." + PoolTaskRest.STEP)
public class PoolTaskStepLinkRepository extends AbstractDSpaceRestRepository implements LinkRestRepository {

    @Autowired
    private PoolTaskService poolTaskService;

    @Autowired
    private XmlWorkflowFactory xmlWorkflowFactory;

    public WorkflowStepRest getStep(@Nullable HttpServletRequest request,
                                    Integer poolTaskId,
                                    @Nullable Pageable optionalPageable,
                                    Projection projection) {

        Context context = obtainContext();
        try {
            PoolTask poolTask = poolTaskService.find(context, poolTaskId);
            if (poolTask == null) {
                throw new ResourceNotFoundException("ClaimedTask with id: " + poolTaskId + " wasn't found");
            }
            return converter.toRest(xmlWorkflowFactory.getStepByName(poolTask.getStepID()), projection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
