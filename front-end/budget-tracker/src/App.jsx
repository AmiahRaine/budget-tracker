import { useState } from 'react'
import { Expenses } from './Expenses'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div>
        <Expenses />
       </div>
    </>
  )
}

export default App
