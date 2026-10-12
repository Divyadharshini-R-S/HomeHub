import { Menu, Bell, Settings, CircleUserRound } from 'lucide-react';
type NavBarProps = {
    setIsExpanded: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function NavBar({setIsExpanded}:NavBarProps){
    return <div className="NavBar">
        <div className="navbar-left">
        <button className="SideBarToggleBtn" onClick={()=>setIsExpanded(prev => !prev)}>
            <Menu />
        </button>
        <h1>HomeHub</h1>
        </div>
        <div className="navbar-right">
        <Bell/> 
        <Settings/>
        <CircleUserRound/>
        </div>
    </div>
}