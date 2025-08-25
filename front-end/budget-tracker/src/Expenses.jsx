import { useQuery } from "@tanstack/react-query";
import { getExpenses } from "./expenses-crud";


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
                                    <button onClick={() => CreateExpenseEditModal(expense.id)}>Edit</button>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </>
    );
}

function CreateExpenseEditModal(id) {
    return (
        <>
            <p style="z-index: 5;">This is the ID: {id}</p>
        </>
    );
}