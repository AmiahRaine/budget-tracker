import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getExpenses, patchExpense, getExpense } from "./expenses-crud";
import { useUpdateCurrentExpense, useUpdateExpenseModalVisible } from "./ExpenseModalContext.jsx";
import React, { useRef } from "react";

export function useGetExpenseData(id) {
    return useQuery({
        queryKey: ["expense", id],
        queryFn: () => getExpense(id)
    });
}

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
                <input type="text" name="amount" defaultValue={expense.amount} />
                <input type="text" name="counterparty" defaultValue={expense.counterparty} />
                <select name="category" defaultValue={expense.category} >
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
                </select>

                {/* Do a patch / post here */}
                <button type="submit">Save & Close</button>
                
                {/* Close without saving */}
                <button onClick={() => setModalVisible(false)}>Close</button>
            </form>
        </>
    );
}


export function Expenses() {
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
            <ExpensesList expenses={expenses}/>
        </>
    );

}


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