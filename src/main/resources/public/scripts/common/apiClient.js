import { showAlert } from "./alerts.js";

async function apiRequest(url, options = {}) {
    try {
        const response = await fetch(url, options);

        if (response.ok) {
            if (response.status === 204) {
                return null;
            }
            const data = await response.json();
            return data;
        } else {
            let errorMessage = `Ошибка ${response.status}: ${response.statusText}`;
            if (response.status === 400 || response.status === 500) {
                const errorData = await response.json();
                errorMessage = errorData.message || errorMessage;
                if (errorData.details) {
                    errorMessage = `${errorMessage}:\n - ${errorData.details.join('; - \n')}.`
                }
            }
            throw new Error(errorMessage);
        }
    } catch (error) {
        console.error('Ошибка запроса к API:', error);
        throw error;
    }
}

export { apiRequest }