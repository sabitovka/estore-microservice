export default {
    id: { text: 'ID', readOnly: true },
    name: { text: 'Наименование товара' },
    price: { text: 'Стоимость', type: 'number'},
    totalCount: { text: 'Общее кол-во', readOnly: true },
    archive: { text: 'Архив', type: 'checkbox' },
    description: { text: 'Описание' },
    electroTypeId: { text: 'Тип электроники'}
};

export const modelInfo = {
    modelName: 'Электро-товары'
}