import NavBar from '../components/NavBar';
import SideBar from '../components/Sidebar';
import { useState } from 'react';

export default function DashboardLayout(){
    const[isExpanded,setIsExpanded] = useState(false);

    return(<div>
        <NavBar setIsExpanded={setIsExpanded}/>
        <SideBar isExpanded={isExpanded}/>   
    </div>);

}
