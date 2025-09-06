import { Expenses } from './Expenses'
import { ExpenseModalProvider} from './ExpenseModalContext.jsx';
import { ExpenseModal } from './Modal.jsx';

function App() {
    
    return (
        <>
            <aside>
                <img src='/public/favicon.svg' />
                <p>Budget Tracker</p>
            </aside>
            <main>
                <ExpenseModalProvider>
                        <ExpenseModal />
                        <Expenses />
                </ExpenseModalProvider>
            </main>
        </>
    )
}

export default App
