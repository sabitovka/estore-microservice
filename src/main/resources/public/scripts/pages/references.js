import { showAlert } from "../common/alerts.js";

const supportedRefs = {
    Shop: {
        model: 'shop',
        extendedBy: 'electroItems',
        fetchAllFunc: 'getElectroItems',
        addItemFunc: 'addElectroItem',
    },
    Employee: {
        model: 'employee',
        extendedBy: 'electroType',
        fetchAllFunc: 'getElectroTypes',
        addItemFunc: 'addElectroType',
    }
}

export async function initializePage() {
    const urlParams = new URLSearchParams(window.location.search);
    const model = urlParams.get('model');
    const extendedBy = urlParams.get('extendedBy');

    if (!model || !extendedBy) {
        showAlert('Не удалось загрузить страницу. Проверьте правильность URL');
        return;
    }

    const modelMeta = await fetchModelData(model);
    const extendedByMeta = await fetchModelData(extendedBy);

    const modelData = await modelMeta.modelApi.fetchData({ size: 100, sortField: 'id' });

    renderTable(
        { ...modelMeta, modelName: model },
        { ...extendedByMeta, modelName: extendedBy },
        modelData,
    );
}

async function fetchModelData(modelName) {
    const modelModule = await import(`../models/${modelName}.js`);
    const model = modelModule.default;
    const { modelInfo } = modelModule;

    const modelApi = await import(`../api/${modelName}Api.js`);
    return { model, modelInfo, modelApi };
}

async function renderTable(model, extendedBy, modelData) {
    const content = document.getElementById('content');

    if (!modelData || modelData.length === 0) {
        content.innerHTML = '<h5 class="text-center mt-5">Данные не найдены</h5>';
        return;
    }

    const modelStructure = model.model;

    const table = document.createElement('table');
    table.className = 'table table-striped';

    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');
    for (const field in modelStructure) {
        const th = document.createElement('th');
        th.textContent = modelStructure[field].text;
        headerRow.appendChild(th);
    }
    headerRow.appendChild(document.createElement('th')).textContent = `Связанные ${extendedBy.modelInfo.modelName}`;
    thead.appendChild(headerRow);
    table.appendChild(thead);

    const tbody = document.createElement('tbody');
    modelData.forEach(async (item) => {
        const row = document.createElement('tr');
        for (const field in modelStructure) {
            const td = document.createElement('td');
            td.textContent = item[field];
            row.appendChild(td);
        }

        const referencedData = model.modelApi[supportedRefs[model.modelName].fetchAllFunc](item.id);
        const relatedTd = document.createElement('td');

        if (model.modelName === 'Shop') {
            referencedData.then((items) => {
                    items.forEach(relatedItem => {
                    const relatedDiv = document.createElement('div');
                    relatedDiv.textContent = `#${relatedItem.id} ${relatedItem.name} (Кол-во: ${relatedItem.count})`;
                    relatedTd.appendChild(relatedDiv);
                });
            });

            const addItemDiv = document.createElement('div');
            addItemDiv.innerHTML = `
                <input type="text" placeholder="ID Товара" class="form-control mb-2" required>
                <input type="number" placeholder="Количество" class="form-control mb-2" min=1 required>
            `
            const addButton = document.createElement('button');
            addButton.classList.add('btn', 'btn-primary');
            addButton.textContent = 'Добавить';
            addButton.addEventListener('click', async (e) => {
                const btn = e.target;
                const parentDiv = btn.parentElement;
                const idInput = parentDiv.children[0];
                const countInput = parentDiv.children[1];

                if (!idInput.value || ! countInput.value) {
                    showAlert('Необходимо заполнить все поля в элементе');
                    return;
                }

                try {
                    await model.modelApi[supportedRefs[model.modelName].addItemFunc](item.id, [{ electroItemId: idInput.value, count: countInput.value }]);
                    window.location.reload();
                } catch (e) {
                    console.error(e);
                    showAlert('Не удалось добавить');
                }
            })
            addItemDiv.appendChild(addButton);
            relatedTd.appendChild(addItemDiv);
        } else if (model.modelName === 'Employee') {
            referencedData.then((items) => {
                items.forEach(relatedItem => {
                    const relatedDiv = document.createElement('div');
                    relatedDiv.textContent = `#${relatedItem.id} ${relatedItem.name}`;

                    const delSpan = document.createElement('span');
                    delSpan.style.cursor = 'pointer';
                    delSpan.classList.add('text-danger', 'float-right');
                    delSpan.textContent = 'D'
                    delSpan.setAttribute('title', 'Удалить')
                    delSpan.addEventListener('click', async () => {
                        try {
                            await model.modelApi.deleteElectroType(item.id, relatedItem.id);
                            window.location.reload();
                        } catch (e) {
                            console.error(e);
                            showAlert('Не удалось удалить');
                        }
                    });

                    relatedDiv.appendChild(delSpan);
                    relatedTd.appendChild(relatedDiv);
                }); 
            });

            const addItemDiv = document.createElement('div');
            addItemDiv.innerHTML = `
                <input type="text" placeholder="ID Типа" class="form-control mb-2" required>
            `
            const addButton = document.createElement('button');
            addButton.classList.add('btn', 'btn-primary');
            addButton.textContent = 'Добавить';
            addButton.addEventListener('click', async (e) => {
                const btn = e.target;
                const parentDiv = btn.parentElement;
                const idInput = parentDiv.children[0];

                if (!idInput.value) {
                    showAlert('Необходимо заполнить все поля в элементе');
                    return;
                }
                
                try {
                    await model.modelApi[supportedRefs[model.modelName].addItemFunc](item.id, idInput.value)
                    window.location.reload()
                } catch (e) {
                    console.error(e);
                    showAlert('Не удалось добавить')
                }
            })
            addItemDiv.appendChild(addButton);
            relatedTd.appendChild(addItemDiv);
        }

        row.appendChild(relatedTd);
        tbody.appendChild(row);
    });

    table.appendChild(tbody);
    content.appendChild(table);
}