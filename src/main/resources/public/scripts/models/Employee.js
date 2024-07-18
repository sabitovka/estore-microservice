export default {
    id: { text: 'ID', readOnly: true },
    firstName: { text: 'Имя' },
    lastName: { text: 'Фамилия' },
    patronymic: { text: 'Отчество' },
    birthDate: { text: 'Дата рождения', type: 'date' },
    gender: { text: 'Мужчина', valueFactory: (value) => (value ? 'М' : 'Ж'), type: 'checkbox' },
    positionTypeId: { text: 'ID должности' },
    shopId: { text: 'ID магазина' }
};

export const modelInfo = {
    modelName: 'Сотрудники'
}