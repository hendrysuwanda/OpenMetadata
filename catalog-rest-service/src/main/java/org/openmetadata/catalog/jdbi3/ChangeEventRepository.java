/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements. See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openmetadata.catalog.jdbi3;

import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.openmetadata.catalog.Entity.EntityList;
import org.openmetadata.catalog.resources.events.EventResource.ChangeEventList;
import org.openmetadata.catalog.type.ChangeEvent;
import org.openmetadata.catalog.type.EventType;
import org.openmetadata.catalog.util.JsonUtils;
import org.openmetadata.catalog.util.RestUtil;
import org.openmetadata.catalog.util.ResultList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChangeEventRepository {
  public static final Logger LOG = LoggerFactory.getLogger(ChangeEventRepository.class);
  private final CollectionDAO dao;

  public ChangeEventRepository(CollectionDAO dao) { this.dao = dao; }

  @Transaction
  public ResultList<ChangeEvent> list(Date date, EntityList entityCreatedList,
                                      EntityList entityUpdatedList, EntityList entityDeletedList) throws IOException,
          GeneralSecurityException {
    String dateStr = RestUtil.DATE_TIME_FORMAT.format(date);
    Timestamp timestamp = new Timestamp(date.getTime());
    System.out.println("Listing from timestamp " + timestamp);
    System.out.println("Listing from timestamp " + timestamp.getTime());
    System.out.println("Listing from time " + date.getTime());
    List<String> jsons = new ArrayList<>();
    jsons.addAll(dao.changeEventDAO().list(EventType.ENTITY_CREATED.value(), entityCreatedList.getList(),
            timestamp.getTime()));
    jsons.addAll(dao.changeEventDAO().list(EventType.ENTITY_UPDATED.value(), entityUpdatedList.getList(),
            timestamp.getTime()));
    jsons.addAll(dao.changeEventDAO().list(EventType.ENTITY_DELETED.value(), entityDeletedList.getList(),
            timestamp.getTime()));
    System.out.println("Total change events " + jsons.size());
    List<ChangeEvent> changeEvents = new ArrayList<>();
    for (String json : jsons) {
      changeEvents.add(JsonUtils.readValue(json, ChangeEvent.class));
    }
    changeEvents.sort(Comparator.comparing((ChangeEvent changeEvent) -> changeEvent.getDateTime().getTime()).reversed());
    return new ChangeEventList(changeEvents, null, null, changeEvents.size()); // TODO
  }

  @Transaction
  public ChangeEvent create(ChangeEvent changeEvent) throws IOException {
    dao.changeEventDAO().insert(JsonUtils.pojoToJson(changeEvent));
    return changeEvent;
  }

}