import { Package, History, Users, Settings } from 'lucide-react';
type SidebarProps={
        isExpanded:boolean;
    };

export default function SideBar({isExpanded}:SidebarProps) {
    return (
        <div className={`sidebar ${isExpanded ? 'expanded' : 'collapsed'}`}>
            <button><Package/>{isExpanded && 'Inventory'}</button>
            <button><History/>{isExpanded && 'History'}</button>
            <button><Users/>{isExpanded && 'Family'}</button>
            <button><Settings/>{isExpanded && 'Settings'}</button>
        </div> 
    );
}