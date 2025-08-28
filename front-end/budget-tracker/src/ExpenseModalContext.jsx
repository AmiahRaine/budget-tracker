import React, { useState, useContext } from 'react';

const ModalVisibleContext = React.createContext();
const UpdateModalVisibleContext = React.createContext();
const CurrentExpenseContext = React.createContext();
const UpdateCurrentExpenseContext = React.createContext();

export function useExpenseModalVisible() {
    return useContext(ModalVisibleContext);
}

export function useUpdateExpenseModalVisible() {
    return useContext(UpdateModalVisibleContext);
}

export function useCurrentExpense() {
    return useContext(CurrentExpenseContext);
}

export function useUpdateCurrentExpense() {
    return useContext(UpdateCurrentExpenseContext);
}

export function ExpenseModalProvider({ children }) {

    const [isExpenseModalVisible, setExpenseModalState] = useState(false);
    const [currentExpenseId, setExpenseId] = useState(null);

    return (

        <ModalVisibleContext.Provider value={isExpenseModalVisible}>
            <UpdateModalVisibleContext.Provider value={setExpenseModalState}>

                <CurrentExpenseContext.Provider value={currentExpenseId}>
                    <UpdateCurrentExpenseContext.Provider value={setExpenseId}>

                        {children}

                    </UpdateCurrentExpenseContext.Provider>
                </CurrentExpenseContext.Provider>

            </UpdateModalVisibleContext.Provider>
        </ModalVisibleContext.Provider>

    );
}