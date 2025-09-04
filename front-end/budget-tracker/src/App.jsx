import { Expenses } from './Expenses'
import { ExpenseModalProvider} from './ExpenseModalContext.jsx';
import { ExpenseModal } from './Modal.jsx';

function App() {
    

    return (
        <>
            <ExpenseModalProvider>
                    
                    <ExpenseModal />
                    <Expenses />
            </ExpenseModalProvider>
        </>
    )
}

export default App
