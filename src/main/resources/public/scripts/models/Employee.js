export default {
    id: { text: "ID", readOnly: true },
    firstName: { text: "Имя" },
    lastName: { text: "Фамилия" },
    patronymic: { text: "Отчество" },
    birthDate: { text: "Дата рождения" },
    gender: { text: "Мужчина", valueFactory: (value) => (value ? 'М' : 'Ж'), type: 'checkbox' }
};

export const modelInfo = {
    modelName: "Сотрудники"
}