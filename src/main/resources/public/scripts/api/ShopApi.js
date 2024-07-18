import { apiRequest } from "../common/apiClient.js";
import { API_BASE_URL } from "../common/constants.js"
import { createParamsString } from "../common/utils.js";

const CONTROLLER_BASE_URL = 'shops';

export async function fetchData(params) {
    const paramsString = createParamsString(params)
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

export async function getElectroItems(shopId) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${shopId}/electro-items`);
}

export async function addElectroItem(shopId, newElectroItems) {
    return apiRequest(`${API_BASE_URL}/${CONTROLLER_BASE_URL}/${shopId}/electro-items`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newElectroItems)
    });
}