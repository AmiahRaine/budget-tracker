import { useCurrentExpense, useExpenseModalVisible } from './ExpenseModalContext';
import { DeleteExpenseButton, PatchExpense, PostExpense } from './Expenses';
import React from 'react';
import './styles/modal.css'

export function ExpenseModal() {
    const currentId = useCurrentExpense();
    const expenseModalVisible = useExpenseModalVisible();

    if (expenseModalVisible) {
        // If there is an id do a PATCH
        if (currentId !== null) return (
            <>
                <div className="background">
                    <div className="modal-surface">
                        <p>HI I AM THE MODAL {currentId}</p>
                        
                        <DeleteExpenseButton id={currentId} />

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
                        <p>HI I AM THE MODAL POST</p>

                        <PostExpense />

                    </div>
                </div>
            </>
        );
    }

    return null;
}


