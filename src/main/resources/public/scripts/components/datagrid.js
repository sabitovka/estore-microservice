export class DataGrid {
    constructor(model, data, modelInfo, { editLinkFactory, deleteEventCallback, sortable = true } = { }, ) {
        this.model = model;
        this.data = data;
        this.modelInfo = modelInfo;
        this.editLinkFactory = editLinkFactory;
        this.deleteEventCallback = deleteEventCallback;
        this.sortable = sortable;
    }

    render() {
        if (this.data.length === 0) {
            const notFound = document.createElement('h5');
            notFound.textContent = 'Данные не найдены';
            notFound.classList.add('text-center', 'mt-5')
            return notFound;
        }

        const table = document.createElement('table');
        table.className = 'table table-striped';

        //заголовок таблицы
        const thead = document.createElement('thead');
        const headerRow = document.createElement('tr');
        for (const field in this.model) {
            const th = document.createElement('th');
            th.textContent = this.model[field].text;
            th.style.cursor = 'pointer';
            this.sortable && th.addEventListener('click', () => this.sortTable(field));
            headerRow.appendChild(th);
        }

        const th = document.createElement('th');
        th.textContent = '#'
        headerRow.appendChild(th);

        thead.appendChild(headerRow);
        table.appendChild(thead);

        // боди таблицы
        const tbody = document.createElement('tbody');
        this.data.forEach(item => {
            const row = document.createElement('tr');
            for (const field in this.model) {
                const td = document.createElement('td');
                if (this.model[field].valueFactory) {
                    td.textContent = this.model[field].valueFactory(item[field]);
                } else {
                    td.textContent = item[field];
                }
                row.appendChild(td);
            }

            const td = document.createElement('td');
            td.classList.add('d-flex', 'justify-content-between')
            
            if (this.modelInfo.editButton !== false) {
                const editLink = document.createElement('a');
                editLink.setAttribute('href', this.editLinkFactory(item.id));
                editLink.setAttribute('title', 'Редактировать');
                editLink.textContent = 'E';
    
                const sep = document.createElement('span');
                sep.textContent = ' | ';

                td.appendChild(editLink);
                td.appendChild(sep);
            }

            if (this.modelInfo.deleteButton !== false) {
                const deleteLink = document.createElement('a');
                deleteLink.setAttribute('href', '');
                deleteLink.setAttribute('title', 'Удалить');
                deleteLink.textContent = 'D';
                deleteLink.addEventListener('click', async (e) => {
                    e.preventDefault();
                    await this.deleteEventCallback(item.id);
                })
                td.appendChild(deleteLink);
            }



            row.appendChild(td);

            tbody.appendChild(row);
        });
        

        table.appendChild(tbody);

        const annotation = document.createElement('small');
        annotation.textContent = 'Для сортировки нажмите на столбец';
        annotation.classList.add('text-danger')

        const div = document.createElement('div');
        this.sortable && div.appendChild(annotation);
        div.appendChild(table);

        return div;
    }

    sortTable(field) {
        const urlParams = new URLSearchParams(window.location.search);
        const currentSortField = urlParams.get('sortField');
        const currentSortType = urlParams.get('sortType') || 'asc';

        let newSortType = 'asc';
        if (currentSortField === field && currentSortType === 'asc') {
            newSortType = 'desc';
        }

        urlParams.set('sortField', field);
        urlParams.set('sortType', newSortType);

        window.location.search = urlParams.toString();
    }
}
