import axios from 'axios';

// Create
export function postExpense(patch) {
    return axios.post("http://localhost:8080/api/expenses", patch).then(res => res.data);
}

// Read
export function getExpenses() {
    return axios.get("http://localhost:8080/api/expenses", { params: { _sort: "name"} }).then(res => res.data);
}

export function getExpense(id) {
    return axios.get(`http://localhost:8080/api/expenses/${id}`).then(res => res.data);
}

// Patch
export function patchExpense(id, patch) {
    return axios.patch(`http://localhost:8080/api/expenses/${id}`, patch).then(res => res.data);
}

// Delete
export function deleteExpense(id) {
    return axios.delete(`http://localhost:8080/api/expenses/${id}`).then(res => res.data);
}



