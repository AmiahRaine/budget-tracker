import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getExpenses, patchExpense, postExpense, getExpense } from "./expenses-crud";
import { useUpdateCurrentExpense, useUpdateExpenseModalVisible } from "./ExpenseModalContext.jsx";
import React, { useRef } from "react";

/**
 * @param {*} id The id of the expense to get.
 * @returns a useQuery() for getting a single expense.
 */
export function useGetExpenseData(id) {
    return useQuery({
        queryKey: ["expense", id],
        queryFn: () => getExpense(id)
    });
}

/**
 * Modifies a single expense. Invalidates the expense list cache when a successful patch happens.
 * @param {*} id The id of the expense to modify.
 * @returns Form for editing the expense.
 */
export function PatchExpense({ id }) {  

    const setModalVisible = useUpdateExpenseModalVisible();
    const formData = useRef(null);
    const queryClient = useQueryClient();

    const {
        status,
        error,
        mutate
    } = useMutation({
        mutationFn: ({id, patch}) => patchExpense(id, patch),
        onSuccess: newData => {
            // Update cache of get one expense
            queryClient.setQueryData(["expenses", id], newData); 
             // Refetch the list to update data
            queryClient.invalidateQueries(["expenses"]);
            // Close the modal
            setModalVisible(false);
        }
    });
    
    const handleSubmit = (e) => {
        e.preventDefault();
        const patchData = Object.fromEntries(new FormData(formData.current).entries());
        console.log(patchData);
        mutate({id, patch: patchData});
    }

    const {
        status: statusOfGet,
        error: errorOfGet,
        data: expense
    } = useGetExpenseData(id);

    if (statusOfGet === "success") return (
        <>

            <form onSubmit={handleSubmit} ref={formData}>
                <input type="text" name="name" defaultValue={expense.name} />
                <input type="number" step="0.01" placeholder="0.00" name="amount" defaultValue={expense.amount} />
                <input type="text" name="counterparty" defaultValue={expense.counterparty} />
                <input type="datetime-local" name="time" defaultValue={expense.time} required />
                <select name="category" defaultValue={expense.category} >
                    <CategoryOptions />
                </select>

                {/* Do a patch / post here */}
                <button type="submit">Save & Close</button>
                
                {/* Close without saving */}
                <button onClick={() => setModalVisible(false)}>Close</button>
            </form>
        </>
    );
}

/**
 * Posts a new expense to the back-end. Invalidates expense list cache when successful.
 * @returns Form for creating the expense.
 */
export function PostExpense() {

    const setModalVisible = useUpdateExpenseModalVisible();
    const formData = useRef(null);
    const queryClient = useQueryClient();

    const {
        status,
        error,
        mutate
    } = useMutation({
        mutationFn: ({post}) => postExpense(post),
        onSuccess: () => {
             // Refetch the list to update data
            queryClient.invalidateQueries(["expenses"]);
            // Close the modal
            setModalVisible(false);
        }
    });
    
    const handleSubmit = (e) => {
        e.preventDefault();
        const postData = Object.fromEntries(new FormData(formData.current).entries());
        console.log(postData);
        mutate({post: postData});
    }

    return (
        <>

            <form onSubmit={handleSubmit} ref={formData}>
                <input type="text" name="name" required />
                <input type="number" step="0.01" placeholder="0.00" name="amount" required />
                <input type="text" name="counterparty" required />
                <input type="datetime-local" name="time" required />
                <select name="category" required >
                    <CategoryOptions />
                </select>

                {/* Do a patch / post here */}
                <button type="submit">Save & Close</button>
                
                {/* Close without saving */}
                <button onClick={() => setModalVisible(false)}>Close</button>
            </form>
        </>
    );
}

/**
 * Primary function for expense CRUD.
 * @returns A list of expenses (and the POST and PATCH buttons).
 */
export function Expenses() {
    const showModal = useUpdateExpenseModalVisible();
    const setId = useUpdateCurrentExpense();

    const {
        status,
        error,
        data: expenses
    } = useQuery({
        queryKey: ["expenses"],
        queryFn: getExpenses
    });

    if (status === "loading") return <h1>Loading...</h1>;
    if (status === "error") return <h1>{JSON.stringify(error)}</h1>;

    return (
        <>
            <h1>Expenses</h1>
            <button onClick={() => {showModal(true); setId(null)}}>Create</button>
            <ExpensesList expenses={expenses}/>
        </>
    );

}

/**
 * @param {*} expenses List of expenses to display.
 * @returns List of expenses formatted into a table.
 */
function ExpensesList({expenses}) {
    const showModal = useUpdateExpenseModalVisible();
    const setId = useUpdateCurrentExpense();

    return (
        <>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Amount</th>
                        <th>Counterparty</th>
                        <th>Category</th>
                    </tr>
                </thead>
                <tbody>
                    {expenses?._embedded?.expenseList.map(expense => {
                        return (
                            <tr key={expense.id}>
                                <td>
                                    {expense.name}
                                </td>
                                <td>
                                    {expense.amount}
                                </td>
                                <td>
                                    {expense.counterpartyText}
                                </td>
                                <td>
                                    {expense.categoryText}
                                </td>
                                <td>
                                    <button onClick={() => {showModal(true); setId(expense.id)}}>Edit</button>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </>
    );
}

/**
 * Options for the category of the expense.
 * @returns Numerous options for a select input tag.
 */
function CategoryOptions() {
    return (
        <>
            <option value="FOOD">Food</option>
            <option value="CLOTHING">Clothing</option>
            <option value="HOUSING">Housing</option>
            <option value="UTILITIES">Utilities</option>
            <option value="ENTERTAINMENT">Entertainment</option>
            <option value="TRANSPORT">Transport</option>
            <option value="BUSINESS">Business</option>
            <option value="EDUCATION">Education</option>
            <option value="MEDICINE">Medicine</option>
            <option value="PERSONAL_CARE">Personal Care</option>
            <option value="PLUSHIES">Plushies</option>
            <option value="OTHER">Other</option>
        </>
    );
}