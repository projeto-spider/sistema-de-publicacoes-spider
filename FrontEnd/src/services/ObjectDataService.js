import http from "../http-common";
import authHeader from './auth-header';
//import Axios from "axios";

class ObjectDataService {
  getAll(object) {
    return http.get(`/${object}`);
  }

  get(object, id) {
    return http.get(`/${object}/${id}`);
  }

  create(object, data) {
    return http.post(`/${object}`, data, { headers: authHeader() });
  }

  createCalendar(object, vehicleId, data) {
    return http.post(`/${object}/${vehicleId}`, data, { headers: authHeader() });
  }

  deleteCalendar(object, vehicleId, calendarId) {
    return http.delete(`/${object}/${vehicleId}/${calendarId}`, { headers: authHeader() });
  }

  sendEmail(user, vehicleName, revisionDate){
    return http.post(`/sendSimpleEmail/${vehicleName}/${revisionDate}`, user);
  }

  sendPlanningEmail(user, vehicleName, revisionDate){
    return http.post(`/sendPlanningEmail/${vehicleName}/${revisionDate}`, user);
  }

  update(object, id, data) {
    return http.put(`/${object}/${id}`, data, { headers: authHeader() });
  }

  createPlan(object, user_id, vehicle_id, data) {
    return http.post(`/${object}/${user_id}_${vehicle_id}`, data, { headers: authHeader() });
  }

  updatePlan(object, planning_id, user_id, vehicle_id, data) {
    return http.put(`/${object}/${planning_id}/${user_id}/${vehicle_id}`, data, { headers: authHeader() })
  }

  updateApprovedPlanRegistered(object, planning_id, new_isRegistered) {
    return http.put(`/${object}/${planning_id}/${new_isRegistered}`, { headers: authHeader() })
  } 

  updatePlanningResubStatus(object, planning_id, new_resubmission, vehicle_resubmitted) {
    return http.put(`/${object}/${planning_id}_${new_resubmission}_${vehicle_resubmitted}`, { headers: authHeader() })
  }

  delete(object, id) {
    return http.delete(`/${object}/${id}`, { headers: authHeader() });
  }

  deleteAll(object) {
    return http.delete(`/${object}`, { headers: authHeader() });
  }

  findByParameter(object, parameter) {
    return http.get(`/${object}?title=${parameter}`);
  }
}

export default new ObjectDataService();