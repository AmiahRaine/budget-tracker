import { useCurrentExpense, useExpenseModalVisible } from './ExpenseModalContext';
import { PatchExpense } from './Expenses';
import React from 'react';
import './styles/modal.css'

export function ExpenseModal() {
    const currentId = useCurrentExpense();
    const expenseModalVisible = useExpenseModalVisible();

    if (expenseModalVisible) return (
        <>
            <div className="background">
                <div className="modal-surface">
                    <p>HI I AM THE MODAL {currentId}</p>

                    <PatchExpense id={currentId} />

                </div>
            </div>
        </>
    );
}


