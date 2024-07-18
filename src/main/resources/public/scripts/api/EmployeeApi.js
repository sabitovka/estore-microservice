import { API_BASE_URL } from "../common/constants.js"
import { createParamsString } from "../common/utils.js";
import { apiRequest } from "../common/apiClient.js";

const CONTROLLER_BASE_URL = 'employees';

export async function fetchData(params) {
    const paramsString = createParamsString(params);
    return await apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}${paramsString}`);
}

export async function deleteElement(id) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${id}`, { method: 'DELETE' });
}

export async function getElementById(id) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${id}`);
}

export async function createElement(data) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(data)
    });
}

export async function updateElementById(id, data) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(data)
    });
}

export async function getElectroTypes(employeeId) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${employeeId}/electro-types`);
}

export async function addElectroType(employeeId, electroTypeId) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${employeeId}/electro-types`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify({ id: electroTypeId }),
    });
}

export async function deleteElectroType(employeeId, electroTypeId) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${employeeId}/electro-types/${electroTypeId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
    });
}