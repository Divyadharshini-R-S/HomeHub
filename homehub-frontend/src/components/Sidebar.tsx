type SidebarProps={
        isExpanded:boolean;
    };

export default function SideBar({isExpanded}:SidebarProps) {
    
    return (
        <div className={`sidebar ${isExpanded ? 'expanded' : 'collapsed'}`}>
            <button><span>Invicon</span>{isExpanded && 'Inventory'}</button>
            <button><span>Historyicon</span>{isExpanded && 'History'}</button>
            <button><span>Familyicon</span>{isExpanded && 'Family'}</button>
            <button><span>Settingsicon</span>{isExpanded && 'Settings'}</button>
        </div> 
    );
}