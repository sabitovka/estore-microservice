import { showAlert } from '../common/alerts.js';
import { Employee, ElectroItem, ElectroType, Shop, PositionType, Purchase, PurchaseType, PurchaseExtra } from '../models/index.js';

const models = { Employee, ElectroItem, ElectroType, Shop, PositionType, Purchase, PurchaseType, PurchaseExtra };


export async function initializePage() {
    const urlParams = new URLSearchParams(window.location.search);
    const modelName = urlParams.get('model');
    const modelId = urlParams.get('modelId');
    const action = urlParams.get('action');

    if (!modelName || !models[modelName]) {
        showAlert('Не указана или не найдена модель');
        return;
    }

    const model = models[modelName];
    const formTitle = action === 'create' ? `Создание ${modelName}` : `Редактирование ${modelName}`;
    document.getElementById('form-title').textContent = formTitle;

    if (action === 'edit' && !modelId) {
        showAlert('Не указан ID для редактирования');
        return;
    }

    const form = document.getElementById('edit-form');
    form.innerHTML = '';

    for (const [field, config] of Object.entries(model)) {
        const formGroup = document.createElement('div');
        formGroup.className = 'form-group mb-2' + (config.type === 'checkbox' ? ' form-check' : '');

        const label = document.createElement('label');
        label.className = config.type === 'checkbox' ? 'form-check-label' : 'form-label';
        label.setAttribute('for', field);
        label.textContent = config.text;

        const input = document.createElement('input');
        input.className = config.type === 'checkbox' ? 'form-check-input' : 'form-control';
        input.id = field;
        input.name = field;
        input.type = config.type || 'text';
        input.readOnly = config.readOnly;

        if (config.type === 'checkbox') {
            formGroup.appendChild(input);
            formGroup.appendChild(label);
        } else {
            formGroup.appendChild(label);
            formGroup.appendChild(input);
        }

        form.appendChild(formGroup);
    }

    if (action === 'edit') {
        try {
            const data = await (await import(`../api/${modelName}Api.js`)).getElementById(modelId);
            for (const [field, value] of Object.entries(data)) {
                const input = document.getElementById(field);
                if (input) {
                    if (input.nodeType === 'checkbox') {

                    }
                    input.value = value;
                }
            }
        } catch (error) {
            console.error('Ошибка загрузки данных', error);
            showAlert('Ошибка загрузки данных');
        }
    }

    document.getElementById('save-button').addEventListener('click', await getSaveDataCallback(modelName, modelId, action, form));
    document.getElementById('cancel-button').addEventListener('click', () => {
        window.location.href = `viewdata.html?model=${modelName}`;
    });
}

async function getSaveDataCallback(modelName, modelId, action, form) {
    return async () => {
        const formData = {};
        for (const element of form.elements) {
            if (element.name) {
                if (element.type === 'checkbox') {
                    formData[element.name] = element.value === 'on';
                } else {
                    formData[element.name] = element.value;
                }
            }
        }

        try {
            if (action === 'create') {
                if (modelName === 'PurchaseExtra') {
                    modelName = 'Purchase';
                }
                await (await import(`../api/${modelName}Api.js`)).createElement(formData);
            } else {
                await (await import(`../api/${modelName}Api.js`)).updateElementById(modelId, formData);
            }
            showAlert('Данные успешно сохранены', 'success');
            window.location.href = `viewdata.html?model=${modelName}`;
        } catch (error) {
            console.error(error);
            showAlert(error.message);
        }
    }
}