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
package org.sentilo.common.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Defines the message structure which is sent to the clients subscribed to any topic
 */
public class EventMessage {

  protected String message;
  protected String timestamp;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String topic;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String type;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String component;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String sensor;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String provider;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String location;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String sender;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String alert;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected String alertType;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  protected Long time;
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  private String sensorType;

  public EventMessage() {

  }

  public String toString() {
    final StringBuilder sb = new StringBuilder("\n--- Notification --- ");
    sb.append("\n\t message:" + message);
    sb.append("\n\t timestamp:" + timestamp);
    sb.append("\n\t time:" + (time != null ? time.longValue() : null));
    sb.append("\n\t topic:" + topic);
    sb.append("\n\t type:" + type);
    sb.append("\n\t provider:" + provider);
    sb.append("\n\t component:" + component);
    sb.append("\n\t sensor:" + sensor);
    sb.append("\n\t location:" + location);
    sb.append("\n\t alert:" + alert);
    sb.append("\n\t alertType:" + alertType);
    sb.append("\n");
    return sb.toString();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(final String topic) {
    this.topic = topic;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final String timestamp) {
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getSensor() {
    return sensor;
  }

  public void setSensor(final String sensor) {
    this.sensor = sensor;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(final String provider) {
    this.provider = provider;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(final String sender) {
    this.sender = sender;
  }

  public void setAlert(final String alert) {
    this.alert = alert;
  }

  public String getAlert() {
    return alert;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(final Long time) {
    this.time = time;
  }

  public String getAlertType() {
    return alertType;
  }

  public void setAlertType(final String alertType) {
    this.alertType = alertType;
  }

  public String getSensorType() {
    return sensorType;
  }

  public void setSensorType(final String sensorType) {
    this.sensorType = sensorType;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(final String component) {
    this.component = component;
  }

}
