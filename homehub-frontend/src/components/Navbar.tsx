type NavBarProps = {
    setIsExpanded: React.Dispatch<React.SetStateAction<boolean>>;
}

export default function NavBar({setIsExpanded}:NavBarProps){
    return <div className="NavBar">
        <button className="SideBarToggleBtn" onClick={()=>setIsExpanded(prev => !prev)}>Toggle</button>
        <h1>HomeHub</h1>
    </div>
}