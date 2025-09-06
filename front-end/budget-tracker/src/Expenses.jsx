import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getExpensesPaged, patchExpense, postExpense, getExpense, deleteExpense, getExpenseBalance } from "./expenses-crud";
import { useUpdateCurrentExpense, useUpdateExpenseModalVisible } from "./ExpenseModalContext.jsx";
import React, { useRef, useState } from "react";
import './styles/expenses.css'

// Used to format amounts shown in table and total balance
const moneyFormatter = Intl.NumberFormat('en-US', {
        style: "currency",
        currency: "USD",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });

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
 * A button for deleting a single expense. Invalidates the expense list cache when a successful delete happens.
 * @param {*} id The id of the expense to delete.
 * @returns A button that when pressed, deletes an expense.
 */
export function DeleteExpenseButton({ id }) {

    const setModalVisible = useUpdateExpenseModalVisible();
    const queryClient = useQueryClient();
    const [confirm, setConfirm] = useState(false);

    const {
        status,
        error,
        mutate
    } = useMutation({
        mutationFn: () => deleteExpense(id),
        onSuccess: () => {
             // Refetch the list to update data
            queryClient.invalidateQueries(["expenses"]);
            // Close the modal
            setModalVisible(false);
        }
    });

    // Make the user double click the delete button to prevent accidental deletes
    if (confirm === false) return (
            <img className="delete-button" src="/public/trash.svg" onClick={() => setConfirm(true)} />
    );
    return (
            <img className="confirm-delete-button" src="/public/trash-delete.svg" onClick={mutate} />
        
    );
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
                <div className="input-fields">
                    <input type="text" name="name" defaultValue={expense.name} />
                    <input type="number" step="0.01" placeholder="0.00" name="amount" defaultValue={expense.amount} />
                    <input type="text" name="counterparty" defaultValue={expense.counterparty} />
                    <input type="datetime-local" name="time" defaultValue={expense.time} required />
                    <select name="category" defaultValue={expense.category} >
                        <CategoryOptions />
                    </select>
                </div>

                <div className="form-buttons">
                    {/* Do a patch / post here */}
                    <button type="submit">Save & Close</button>
                    
                    {/* Close without saving */}
                    <button onClick={() => setModalVisible(false)}>Close</button>
                </div>
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
                <div className="input-fields">
                    <input type="text" name="name" placeholder="Label" required />
                    <input type="number" step="0.01" placeholder="0.00" name="amount" required />
                    <input type="text" name="counterparty" placeholder="Counterparty" required />
                    <input type="datetime-local" name="time" required />
                    <select name="category" required >
                        <CategoryOptions />
                    </select>
                </div>

                <div className="form-buttons">
                    {/* Do a patch / post here */}
                    <button type="submit">Save & Close</button>
                    
                    {/* Close without saving */}
                    <button onClick={() => setModalVisible(false)}>Close</button>
                </div>
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
    const [page, setPage] = useState(0);
    const pageSize = 20;

    const {
        status,
        error,
        data: expenses
    } = useQuery({
        queryKey: ["expenses", page, pageSize],
        queryFn: () => getExpensesPaged(page, pageSize)
    });

    if (status === "loading") return <h1>Loading...</h1>;
    if (status === "error") return <h1>{JSON.stringify(error)}</h1>;

    return (
        <>
            <div className="all-expense-info">

                <div className="expense-header">
                    <h1><TotalExpenseBalance /></h1>

                    <button onClick={() => {showModal(true); setId(null)}}>Create</button>
                </div>

                <div className="expense-list">
                    <ExpensesList expenses={expenses}/>

                    <div className="pagination-buttons">
                        {/* Pagination buttons */}
                        {(page > 0) && <button onClick={() => {setPage(prev => prev - 1)}}>Prev</button>}
                        {isNextAvailable(expenses) && <button onClick={() => setPage(prev => prev + 1)}>Next</button>}
                    </div>

                </div>
            </div>
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

    function formatTableExpense(amount) {
        let a = moneyFormatter.format(amount);

        if (!a.startsWith('-')) {
            return (
                <>
                    <span className="spacer" />{a}
                </>
            );
        }
        return (<>{a}</>);
    }

    return (
        <>
            <table className="expenses-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Amount</th>
                        <th>Counterparty</th>
                        <th>Category</th>
                        <th>Time</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {expenses?._embedded?.expenseList.map((expense, index) => {
                        return (
                            <tr key={expense.id} className={(index % 2) === 0 ? "even-row" : "odd-row"}>
                                <td>
                                    {expense.name}
                                </td>
                                <td>
                                    {formatTableExpense(expense.amount)}
                                </td>
                                <td>
                                    {expense.counterpartyText}
                                </td>
                                <td>
                                    {expense.categoryText}
                                </td>
                                <td>
                                    {expense.timeFormatted}
                                </td>
                                <td className="edit-td">
                                    <button className="edit-button" onClick={() => {showModal(true); setId(expense.id)}}>Edit</button>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </>
    );
}

export function TotalExpenseBalance() {

    const {
        status,
        error,
        data: total
    } = useQuery({
        queryKey: ["expenses-total",],
        queryFn: getExpenseBalance
    });

    if (status === "loading") return <h1>Loading...</h1>;
    if (status === "error") return <h1>{JSON.stringify(error)}</h1>;

    return (
        <>
            {moneyFormatter.format(total?.total)}
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

/**
 * @param {*} expenses Results of paginated fetch query 
 * @returns True if there are more pages, false otherwise.
 */
function isNextAvailable(expenses) {
    if (expenses?._links?.next) return true;
    return false;
}