export default {
    id: { text: 'ID', readOnly: true },
    electro: { text: 'Товар', valueFactory: (value) => (value.name) },
    employee: { text: 'Сотрудник', valueFactory: (value) => (`${value.firstName} ${value.lastName}`) },
    shop: { text: 'Магазин', valueFactory: (value) => (value.name) },
    purchaseDate: { text: 'Дата покупки', valueFactory: (value) => (new Date(value).toLocaleString()), readOnly: true},
    type: { text: 'Тип оплаты', valueFactory: (value) => (value.name) }
};

export const extra = {
    electroItemId : { text: 'ID товара' },
    employeeId: { text: 'ID сотрудника' },
    shopId: { text: 'ID магазина' },
    purchaseTypeId: { text: 'ID типа оплаты' }
}

export const modelInfo = {
    modelName: "Покупки",
    editButton: false,
    deleteButton: false,
}