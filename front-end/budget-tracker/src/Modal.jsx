import { useCurrentExpense, useExpenseModalVisible } from './ExpenseModalContext';
import { DeleteExpenseButton, PatchExpense, PostExpense } from './Expenses';
import React from 'react';
import './styles/modal.css'
import './styles/expenses.css'

export function ExpenseModal() {
    const currentId = useCurrentExpense();
    const expenseModalVisible = useExpenseModalVisible();

    if (expenseModalVisible) {
        // If there is an id do a PATCH
        if (currentId !== null) return (
            <>
                <div className="background">
                    <div className="modal-surface">
                        <span className="delete-button-container">
                            <h2>Editing Budget Item</h2>
                            <DeleteExpenseButton id={currentId} />
                        </span>

                        <PatchExpense id={currentId} />

                    </div>
                </div>
            </>
        );
        // If no id do a POST
        else return (
            <>
                <div className="background">
                    <div className="modal-surface">
                        <h2>Create Budget Item</h2>

                        <PostExpense />
                    </div>
                </div>
            </>
        );
    }

    return null;
}


