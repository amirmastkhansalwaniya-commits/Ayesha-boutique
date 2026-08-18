import React, { useState, useEffect, useMemo } from 'react';
import { 
  Scissors, Plus, Search, Settings, Phone, Calendar, 
  DollarSign, CheckCircle2, AlertCircle, Clock, Users, 
  ShoppingBag, Trash2, Edit3, Image as ImageIcon, Sparkles, 
  ChevronRight, Share2, ShieldCheck, X, Check, ArrowRight,
  Filter, Download, Upload, RefreshCw, Layers, Palette,
  Ruler, MapPin, Printer, Copy, CheckCircle
} from 'lucide-react';

const GARMENT_TYPES = [
  "Bridal Lehanga & Kurti",
  "3-Piece Tuxedo / Suit",
  "Salwar Kameez (Party Wear)",
  "Embroidered Anarkali Suit",
  "Abaya / Luxury Kaftan",
  "Formal Sherwani",
  "Velvet Evening Gown",
  "Kurta Pajama",
  "Blouse / Saree Couture",
  "Bespoke Couture"
];

const STANDARD_SIZES = ["Custom", "XS", "S", "M (38)", "L (40)", "XL (42)", "2XL", "3XL"];

const STATUS_STEPS = [
  'Order Taken',
  'Cutting',
  'Stitching',
  'Fitting',
  'Ready',
  'Delivered'
];

const THEME_PRESETS = [
  { id: 'royal_maroon', name: 'Royal Maroon & Gold', primary: '#D4AF37', bright: '#F5D77F', bg: '#160408', surface: '#22070D', card: '#3A0F1A', border: '#D4AF37' },
  { id: 'emerald_velvet', name: 'Emerald Velvet', primary: '#10B981', bright: '#34D399', bg: '#031710', surface: '#06261B', card: '#0D3829', border: '#10B981' },
  { id: 'midnight_sapphire', name: 'Midnight Sapphire', primary: '#60A5FA', bright: '#93C5FD', bg: '#060E1A', surface: '#0A182E', card: '#112544', border: '#3B82F6' },
  { id: 'obsidian_noir', name: 'Obsidian Noir & Rose', primary: '#FB7185', bright: '#FDA4AF', bg: '#0F0F10', surface: '#1A1A1E', card: '#25252B', border: '#F43F5E' }
];

export default function App() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const [searchQuery, setSearchQuery] = useState('');
  const [orderFilter, setOrderFilter] = useState('ALL');

  // Settings
  const [settings, setSettings] = useState(() => {
    try {
      const saved = localStorage.getItem('aysha_settings');
      return saved ? JSON.parse(saved) : {
        boutiqueName: 'AYSHA BOUTIQUE',
        tagline: 'Haute Couture & Bespoke Tailoring Atelier',
        currency: '₹',
        themeId: 'royal_maroon',
        selectedLogo: '✂️',
        customLogoUrl: null,
        creatorCredit: 'This app created by Amir Khan',
        phone: '+91 98765 43210',
        address: 'Main Atelier, Luxury Fashion Hub'
      };
    } catch {
      return {
        boutiqueName: 'AYSHA BOUTIQUE',
        tagline: 'Haute Couture & Bespoke Tailoring Atelier',
        currency: '₹',
        themeId: 'royal_maroon',
        selectedLogo: '✂️',
        customLogoUrl: null,
        creatorCredit: 'This app created by Amir Khan',
        phone: '+91 98765 43210',
        address: 'Main Atelier, Luxury Fashion Hub'
      };
    }
  });

  // Orders
  const [orders, setOrders] = useState(() => {
    try {
      const saved = localStorage.getItem('aysha_orders');
      return saved ? JSON.parse(saved) : [
        {
          id: 1001,
          orderNumber: 'AY-1001',
          customerName: 'Fatima Sheikh',
          customerPhone: '+91 98765 43210',
          customerAddress: 'Civil Lines, Palace Road',
          suitType: 'Bridal Lehanga & Embellished Choli',
          fabricDetails: 'Pure Raw Silk with Zardozi Work & Heavy Dabka Handwork',
          numberOfSuits: 1,
          standardSize: 'Custom',
          totalAmount: 18500,
          receivedAmount: 12000,
          dateTaken: '2026-08-10',
          dateDueForDelivery: '2026-08-20',
          status: 'Stitching',
          specialInstructions: 'Deep back neckline with heavy dori latkan. Extra margin inside.',
          measurements: {
            unit: 'in',
            chest: '36',
            waist: '30',
            hips: '38',
            shoulder: '14.5',
            sleeve: '18',
            trouserLength: '42',
            neck: '7',
            inseam: '30',
            armhole: '16',
            notes: 'Fitted bodice, circular flare'
          }
        },
        {
          id: 1002,
          orderNumber: 'AY-1002',
          customerName: 'Zainab Qureshi',
          customerPhone: '+91 91234 56789',
          customerAddress: 'Old City, Heritage Lane',
          suitType: 'Embroidered Anarkali Suit',
          fabricDetails: 'Georgette with Resham Threadwork & Mirror Borders',
          numberOfSuits: 2,
          standardSize: 'M (38)',
          totalAmount: 9500,
          receivedAmount: 9500,
          dateTaken: '2026-08-12',
          dateDueForDelivery: '2026-08-24',
          status: 'Fitting',
          specialInstructions: 'Churidar sleeves with small gold buttons.',
          measurements: {
            unit: 'in',
            chest: '38',
            waist: '32',
            hips: '40',
            shoulder: '15',
            sleeve: '21',
            trouserLength: '40',
            neck: '6.5',
            inseam: '29',
            armhole: '17',
            notes: 'Comfort fit'
          }
        },
        {
          id: 1003,
          orderNumber: 'AY-1003',
          customerName: 'Amina Begum',
          customerPhone: '+91 99887 76655',
          customerAddress: 'Gulshan Nagar',
          suitType: 'Salwar Kameez (Party Wear)',
          fabricDetails: 'Banarasi Brocade with Organza Dupatta',
          numberOfSuits: 1,
          standardSize: 'L (40)',
          totalAmount: 5200,
          receivedAmount: 2000,
          dateTaken: '2026-08-14',
          dateDueForDelivery: '2026-08-19',
          status: 'Cutting',
          specialInstructions: 'Straight cut pant with side pockets.',
          measurements: {
            unit: 'in',
            chest: '40',
            waist: '34',
            hips: '42',
            shoulder: '15.5',
            sleeve: '19',
            trouserLength: '39',
            neck: '6.5',
            inseam: '28',
            armhole: '18',
            notes: 'Standard fit'
          }
        }
      ];
    } catch {
      return [];
    }
  });

  // Clients
  const [clients, setClients] = useState(() => {
    try {
      const saved = localStorage.getItem('aysha_clients');
      return saved ? JSON.parse(saved) : [
        { 
          id: 1, 
          name: 'Fatima Sheikh', 
          phone: '+91 98765 43210', 
          address: 'Civil Lines, Palace Road',
          chest: '36', 
          waist: '30', 
          hips: '38', 
          shoulder: '14.5',
          sleeve: '18',
          trouserLength: '42',
          neck: '7',
          inseam: '30',
          armhole: '16',
          unit: 'in',
          notes: 'Prefers deep neck cuts and flared bottoms'
        },
        { 
          id: 2, 
          name: 'Zainab Qureshi', 
          phone: '+91 91234 56789', 
          address: 'Old City, Heritage Lane',
          chest: '38', 
          waist: '32', 
          hips: '40', 
          shoulder: '15',
          sleeve: '21',
          trouserLength: '40',
          neck: '6.5',
          inseam: '29',
          armhole: '17',
          unit: 'in',
          notes: 'Standard churidar sleeves lover'
        },
        { 
          id: 3, 
          name: 'Amina Begum', 
          phone: '+91 99887 76655', 
          address: 'Gulshan Nagar',
          chest: '40', 
          waist: '34', 
          hips: '42', 
          shoulder: '15.5',
          sleeve: '19',
          trouserLength: '39',
          neck: '6.5',
          inseam: '28',
          armhole: '18',
          unit: 'in',
          notes: 'Pant style bottoms with pocket'
        }
      ];
    } catch {
      return [];
    }
  });

  // Modals & Active state
  const [showOrderModal, setShowOrderModal] = useState(false);
  const [orderToEdit, setOrderToEdit] = useState(null);

  const [showClientModal, setShowClientModal] = useState(false);
  const [clientToEdit, setClientToEdit] = useState(null);

  const [selectedOrder, setSelectedOrder] = useState(null);
  const [showRecordPaymentModal, setShowRecordPaymentModal] = useState(false);
  const [paymentAmountInput, setPaymentAmountInput] = useState('');

  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // Sync to local storage
  useEffect(() => {
    try {
      localStorage.setItem('aysha_settings', JSON.stringify(settings));
    } catch {}
  }, [settings]);

  useEffect(() => {
    try {
      localStorage.setItem('aysha_orders', JSON.stringify(orders));
    } catch {}
  }, [orders]);

  useEffect(() => {
    try {
      localStorage.setItem('aysha_clients', JSON.stringify(clients));
    } catch {}
  }, [clients]);

  // Calculations
  const totalRevenue = useMemo(() => orders.reduce((acc, o) => acc + Number(o.totalAmount || 0), 0), [orders]);
  const totalCollected = useMemo(() => orders.reduce((acc, o) => acc + Number(o.receivedAmount || 0), 0), [orders]);
  const totalPending = totalRevenue - totalCollected;

  const fullyPaidCount = useMemo(() => orders.filter(o => Number(o.receivedAmount || 0) >= Number(o.totalAmount || 0) && Number(o.totalAmount || 0) > 0).length, [orders]);
  const partialPaidCount = useMemo(() => orders.filter(o => Number(o.receivedAmount || 0) > 0 && Number(o.receivedAmount || 0) < Number(o.totalAmount || 0)).length, [orders]);
  const unpaidCount = useMemo(() => orders.filter(o => Number(o.receivedAmount || 0) === 0).length, [orders]);

  // Urgent Orders (due within 3 days or overdue)
  const urgentOrders = useMemo(() => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return orders.filter(o => {
      if (o.status === 'Delivered') return false;
      const due = new Date(o.dateDueForDelivery);
      due.setHours(0, 0, 0, 0);
      const diffDays = Math.ceil((due - today) / (1000 * 60 * 60 * 24));
      return diffDays <= 3;
    });
  }, [orders]);

  // Filtered Orders
  const filteredOrders = useMemo(() => {
    return orders.filter(o => {
      const matchesSearch = 
        (o.customerName || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
        (o.orderNumber || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
        (o.suitType || '').toLowerCase().includes(searchQuery.toLowerCase()) ||
        (o.customerPhone || '').includes(searchQuery) ||
        (o.fabricDetails || '').toLowerCase().includes(searchQuery.toLowerCase());

      if (!matchesSearch) return false;

      if (orderFilter === 'ALL') return true;
      if (orderFilter === 'ACTIVE') return o.status !== 'Delivered';
      if (orderFilter === 'PENDING_PAYMENT') return (Number(o.totalAmount || 0) - Number(o.receivedAmount || 0)) > 0;
      if (orderFilter === 'URGENT') {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const due = new Date(o.dateDueForDelivery);
        due.setHours(0, 0, 0, 0);
        const diffDays = Math.ceil((due - today) / (1000 * 60 * 60 * 24));
        return o.status !== 'Delivered' && diffDays <= 3;
      }
      return o.status === orderFilter;
    });
  }, [orders, searchQuery, orderFilter]);

  // Logo upload
  const handleLogoUpload = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        setSettings(prev => ({ ...prev, customLogoUrl: event.target?.result }));
        showToast("लोगो सफलतापूर्वक अपडेट किया गया!");
      };
      reader.readAsDataURL(file);
    }
  };

  // Export Data JSON
  const handleExportBackup = () => {
    const data = {
      settings,
      orders,
      clients,
      exportDate: new Date().toISOString()
    };
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `Aysha_Boutique_Backup_${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    showToast("डेटा बैकअप फ़ाइल डाउनलोड हो गई!");
  };

  // Import Data JSON
  const handleImportBackup = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (evt) => {
        try {
          const parsed = JSON.parse(evt.target.result);
          if (parsed.settings) setSettings(parsed.settings);
          if (parsed.orders) setOrders(parsed.orders);
          if (parsed.clients) setClients(parsed.clients);
          showToast("डेटा बैकअप सफलतापूर्वक लोड हुआ!");
        } catch {
          alert("अमान्य बैकअप फ़ाइल!");
        }
      };
      reader.readAsText(file);
    }
  };

  // Generate Receipt text
  const generateReceiptText = (order) => {
    const pending = Number(order.totalAmount || 0) - Number(order.receivedAmount || 0);
    return `==============================\n✨ *${settings.boutiqueName.toUpperCase()}* ✨\n${settings.tagline}\n==============================\n📋 *INVOICE / RECEIPT*\nOrder ID: ${order.orderNumber}\nDate Taken: ${order.dateTaken}\nDelivery Due: ${order.dateDueForDelivery}\nStatus: ${order.status}\n\n👤 *CLIENT DETAILS*\nName: ${order.customerName}\nPhone: ${order.customerPhone}\nAddress: ${order.customerAddress || 'N/A'}\n\n👗 *GARMENT SPECIFICATIONS*\nItem: ${order.suitType}\nSuits: ${order.numberOfSuits} | Size: ${order.standardSize || 'Custom'}\nFabric: ${order.fabricDetails || 'N/A'}\nInstructions: ${order.specialInstructions || 'N/A'}\n\n📐 *BODY MEASUREMENTS (${order.measurements?.unit || 'in'})*\nChest: ${order.measurements?.chest || '-'} | Waist: ${order.measurements?.waist || '-'} | Hips: ${order.measurements?.hips || '-'}\nShoulder: ${order.measurements?.shoulder || '-'} | Sleeve: ${order.measurements?.sleeve || '-'} | Trouser L.: ${order.measurements?.trouserLength || '-'}\nNeck: ${order.measurements?.neck || '-'} | Inseam: ${order.measurements?.inseam || '-'} | Armhole: ${order.measurements?.armhole || '-'}\n\n💰 *FINANCIAL ACCOUNTING*\nTotal Amount: ${settings.currency}${order.totalAmount}\nAdvance Received: ${settings.currency}${order.receivedAmount}\nBalance Due: ${settings.currency}${pending}\nPayment Status: ${pending <= 0 ? 'PAID IN FULL ✅' : 'PENDING ⚠️'}\n==============================\n_${settings.creatorCredit}_\nThank you for choosing luxury couture!`;
  };

  return (
    <div className="min-h-screen bg-[#160408] text-[#F8F1E5] flex justify-center font-sans antialiased selection:bg-[#D4AF37] selection:text-[#160408]">
      <div className="w-full max-w-md min-h-screen bg-[#160408] border-x border-[#D4AF37]/30 flex flex-col relative shadow-2xl pb-24">
        
        {/* Toast Notification */}
        {toastMessage && (
          <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-[#D4AF37] text-[#160408] font-bold text-xs px-4 py-2 rounded-full shadow-2xl border border-white/40 flex items-center gap-2 animate-bounce">
            <CheckCircle className="w-4 h-4" />
            <span>{toastMessage}</span>
          </div>
        )}

        {/* Top Header */}
        <header className="sticky top-0 z-40 bg-gradient-to-b from-[#160408] via-[#22070D] to-[#2E0B13] border-b border-[#D4AF37]/30 p-3.5 backdrop-blur-md">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="w-11 h-11 rounded-full border-2 border-[#D4AF37] bg-[#3A0F1A] flex items-center justify-center overflow-hidden flex-shrink-0 shadow-lg ring-2 ring-[#D4AF37]/20">
                {settings.customLogoUrl ? (
                  <img src={settings.customLogoUrl} alt="Logo" className="w-full h-full object-cover" />
                ) : (
                  <span className="text-xl">{settings.selectedLogo}</span>
                )}
              </div>
              <div className="overflow-hidden">
                <h1 className="text-sm font-bold tracking-wider text-[#F5D77F] font-serif uppercase truncate">
                  {settings.boutiqueName}
                </h1>
                <p className="text-[11px] text-[#C4B6A6] truncate max-w-[190px]">
                  {settings.tagline}
                </p>
              </div>
            </div>

            <div className="flex items-center gap-1.5">
              <button 
                onClick={() => {
                  setOrderToEdit(null);
                  setShowOrderModal(true);
                }}
                className="w-8 h-8 rounded-lg bg-[#D4AF37] text-[#160408] flex items-center justify-center font-bold hover:bg-[#F5D77F] transition shadow"
                title="New Order"
              >
                <Plus className="w-4 h-4" />
              </button>
              <button 
                onClick={() => setCurrentTab('settings')}
                className="w-8 h-8 rounded-lg bg-[#2E0B13] border border-[#D4AF37]/50 flex items-center justify-center text-[#F5D77F] hover:bg-[#3A0F1A] transition"
                title="Settings"
              >
                <Settings className="w-4 h-4" />
              </button>
            </div>
          </div>

          {/* Search bar */}
          {currentTab !== 'settings' && (
            <div className="mt-3 relative">
              <Search className="w-4 h-4 absolute left-3 top-2.5 text-[#D4AF37]/70" />
              <input 
                type="text"
                placeholder="Search orders, clients, phone, fabrics..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full bg-[#160408]/90 border border-[#D4AF37]/30 rounded-xl pl-9 pr-8 py-2 text-xs text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37] placeholder-[#C4B6A6]/50 shadow-inner"
              />
              {searchQuery && (
                <button 
                  onClick={() => setSearchQuery('')}
                  className="absolute right-2.5 top-2.5 text-[#C4B6A6] hover:text-white"
                >
                  <X className="w-3.5 h-3.5" />
                </button>
              )}
            </div>
          )}
        </header>

        {/* Content Screens */}
        <main className="p-4 flex-1 space-y-4 overflow-y-auto">
          
          {/* TAB 1: ATELIER DASHBOARD */}
          {currentTab === 'dashboard' && (
            <>
              {/* Hero Banner */}
              <div className="rounded-2xl p-4 bg-gradient-to-b from-[#521524]/80 to-[#3A0F1A] border border-[#D4AF37]/50 shadow-xl relative overflow-hidden">
                <div className="absolute top-0 right-0 w-32 h-32 bg-[#D4AF37]/5 rounded-full blur-2xl pointer-events-none" />
                
                <span className="text-[10px] font-bold tracking-widest text-[#F5D77F] uppercase block">
                  {new Date().toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric', year: 'numeric' })}
                </span>
                <h2 className="text-xl font-serif font-bold text-[#F8F1E5] mt-0.5">{settings.boutiqueName}</h2>
                <p className="text-[11px] text-[#C4B6A6]">Atelier Operations, Bespoke Tailoring & Account Ledger</p>

                <div className="grid grid-cols-2 gap-2 mt-4">
                  <button 
                    onClick={() => {
                      setOrderToEdit(null);
                      setShowOrderModal(true);
                    }}
                    className="flex items-center justify-center gap-1.5 bg-[#D4AF37] hover:bg-[#F5D77F] text-[#160408] font-bold text-xs py-2 px-3 rounded-xl transition shadow-lg"
                  >
                    <Plus className="w-4 h-4" /> New Order
                  </button>
                  <button 
                    onClick={() => {
                      setClientToEdit(null);
                      setShowClientModal(true);
                    }}
                    className="flex items-center justify-center gap-1.5 bg-[#2E0B13] hover:bg-[#3A0F1A] border border-[#D4AF37]/60 text-[#F5D77F] font-bold text-xs py-2 px-3 rounded-xl transition"
                  >
                    <Users className="w-4 h-4" /> New Client
                  </button>
                </div>
              </div>

              {/* Urgent Deliveries Banner (if any) */}
              {urgentOrders.length > 0 && (
                <div className="bg-[#521524]/60 border border-[#F43F5E]/60 rounded-xl p-3 flex items-center justify-between">
                  <div className="flex items-center gap-2.5">
                    <div className="w-8 h-8 rounded-lg bg-[#F43F5E]/20 text-[#FDA4AF] flex items-center justify-center flex-shrink-0">
                      <AlertCircle className="w-4 h-4" />
                    </div>
                    <div>
                      <h4 className="text-xs font-bold text-[#FDA4AF]">Urgent Deliveries ({urgentOrders.length})</h4>
                      <p className="text-[10px] text-[#C4B6A6]">Due within 3 days or today</p>
                    </div>
                  </div>
                  <button 
                    onClick={() => {
                      setOrderFilter('URGENT');
                      setCurrentTab('orders');
                    }}
                    className="text-xs font-bold text-[#F5D77F] bg-[#160408] px-2.5 py-1 rounded-lg border border-[#D4AF37]/40 flex items-center gap-1 hover:bg-[#3A0F1A]"
                  >
                    View <ChevronRight className="w-3 h-3" />
                  </button>
                </div>
              )}

              {/* Quick Key Metrics */}
              <div className="grid grid-cols-2 gap-2.5">
                <div 
                  onClick={() => { setOrderFilter('ALL'); setCurrentTab('orders'); }}
                  className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3 flex justify-between items-center cursor-pointer hover:border-[#D4AF37] transition"
                >
                  <div>
                    <span className="text-[11px] text-[#C4B6A6]">Total Orders</span>
                    <div className="text-lg font-bold text-[#F8F1E5]">{orders.length}</div>
                    <span className="text-[10px] text-[#F5D77F]">{orders.filter(o => o.status !== 'Delivered').length} active in production</span>
                  </div>
                  <div className="w-9 h-9 rounded-full bg-[#521524] flex items-center justify-center text-[#F5D77F]">
                    <ShoppingBag className="w-4 h-4" />
                  </div>
                </div>

                <div 
                  onClick={() => { setOrderFilter('PENDING_PAYMENT'); setCurrentTab('finances'); }}
                  className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3 flex justify-between items-center cursor-pointer hover:border-[#D4AF37] transition"
                >
                  <div>
                    <span className="text-[11px] text-[#C4B6A6]">Pending Dues</span>
                    <div className="text-lg font-bold text-[#F59E0B]">{settings.currency}{totalPending.toLocaleString()}</div>
                    <span className="text-[10px] text-[#C4B6A6]">{orders.filter(o => (o.totalAmount - o.receivedAmount) > 0).length} unpaid orders</span>
                  </div>
                  <div className="w-9 h-9 rounded-full bg-[#521524] flex items-center justify-center text-[#F59E0B]">
                    <DollarSign className="w-4 h-4" />
                  </div>
                </div>
              </div>

              {/* Financial Accounting Snapshot */}
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3.5 space-y-3">
                <div className="flex justify-between items-center">
                  <div className="flex items-center gap-1.5">
                    <DollarSign className="w-4 h-4 text-[#D4AF37]" />
                    <span className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">Financial Overview</span>
                  </div>
                  <span className="text-[10px] bg-[#160408] px-2.5 py-0.5 rounded-full text-[#10B981] font-semibold border border-[#10B981]/30">
                    {totalRevenue ? Math.round((totalCollected / totalRevenue) * 100) : 0}% Collected
                  </span>
                </div>

                {/* Progress Bar */}
                <div className="w-full bg-[#160408] h-2 rounded-full overflow-hidden border border-[#D4AF37]/20 flex">
                  <div 
                    className="bg-[#10B981] h-full transition-all duration-500" 
                    style={{ width: `${totalRevenue ? Math.min(100, (totalCollected / totalRevenue) * 100) : 0}%` }}
                  />
                  <div 
                    className="bg-[#F59E0B] h-full transition-all duration-500" 
                    style={{ width: `${totalRevenue ? Math.min(100, (totalPending / totalRevenue) * 100) : 0}%` }}
                  />
                </div>

                <div className="grid grid-cols-3 gap-2 text-center">
                  <div className="bg-[#160408] p-2 rounded-lg border border-[#D4AF37]/20">
                    <span className="text-[9px] text-[#C4B6A6] block">Total Billed</span>
                    <span className="text-xs font-bold text-[#F8F1E5]">{settings.currency}{totalRevenue.toLocaleString()}</span>
                  </div>
                  <div className="bg-[#160408] p-2 rounded-lg border border-[#10B981]/30">
                    <span className="text-[9px] text-[#C4B6A6] block">Advance Recv.</span>
                    <span className="text-xs font-bold text-[#10B981]">{settings.currency}{totalCollected.toLocaleString()}</span>
                  </div>
                  <div className="bg-[#160408] p-2 rounded-lg border border-[#F59E0B]/30">
                    <span className="text-[9px] text-[#C4B6A6] block">Balance Due</span>
                    <span className="text-xs font-bold text-[#F59E0B]">{settings.currency}{totalPending.toLocaleString()}</span>
                  </div>
                </div>
              </div>

              {/* Status Production Pipeline */}
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3.5 space-y-2">
                <span className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider block">Production Pipeline</span>
                <div className="grid grid-cols-3 gap-2">
                  {STATUS_STEPS.slice(0, 5).map(status => {
                    const count = orders.filter(o => o.status === status).length;
                    return (
                      <button
                        key={status}
                        onClick={() => { setOrderFilter(status); setCurrentTab('orders'); }}
                        className="bg-[#160408] border border-[#D4AF37]/20 rounded-lg p-2 text-left hover:border-[#D4AF37] transition"
                      >
                        <span className="text-[10px] text-[#C4B6A6] block truncate">{status}</span>
                        <span className="text-sm font-bold text-[#F5D77F]">{count}</span>
                      </button>
                    );
                  })}
                  <button
                    onClick={() => { setOrderFilter('Delivered'); setCurrentTab('orders'); }}
                    className="bg-[#160408] border border-[#10B981]/30 rounded-lg p-2 text-left hover:border-[#10B981] transition"
                  >
                    <span className="text-[10px] text-[#10B981] block truncate">Delivered</span>
                    <span className="text-sm font-bold text-[#10B981]">{orders.filter(o => o.status === 'Delivered').length}</span>
                  </button>
                </div>
              </div>

              {/* Recent Orders List */}
              <div className="space-y-2.5">
                <div className="flex justify-between items-center">
                  <span className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">Recent Orders</span>
                  <button 
                    onClick={() => { setOrderFilter('ALL'); setCurrentTab('orders'); }} 
                    className="text-xs text-[#D4AF37] hover:underline flex items-center gap-0.5"
                  >
                    View All ({orders.length}) <ChevronRight className="w-3 h-3" />
                  </button>
                </div>

                {filteredOrders.slice(0, 4).map(order => (
                  <OrderCard 
                    key={order.id} 
                    order={order} 
                    currency={settings.currency} 
                    onView={() => setSelectedOrder(order)} 
                  />
                ))}

                {filteredOrders.length === 0 && (
                  <div className="text-center py-6 bg-[#3A0F1A] rounded-xl border border-[#D4AF37]/20 p-4">
                    <ShoppingBag className="w-8 h-8 text-[#D4AF37]/40 mx-auto mb-2" />
                    <p className="text-xs text-[#C4B6A6]">No orders match your search.</p>
                  </div>
                )}
              </div>

              {/* Creator Credit Box on Dashboard */}
              <div className="rounded-xl p-3 bg-gradient-to-b from-[#2E0B13] to-[#160408] border border-[#D4AF37]/50 text-center space-y-0.5">
                <div className="flex items-center justify-center gap-1 text-[#F5D77F] text-[10px] font-bold tracking-widest uppercase">
                  <Sparkles className="w-3 h-3 text-[#D4AF37]" /> Official Atelier Suite
                </div>
                <h4 className="text-xs font-serif font-bold text-[#F5D77F]">
                  This app created by Amir Khan
                </h4>
                <p className="text-[10px] text-[#C4B6A6]">Crafted for {settings.boutiqueName}</p>
              </div>
            </>
          )}

          {/* TAB 2: ORDERS LIST & FILTERS */}
          {currentTab === 'orders' && (
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">
                    Orders Directory ({filteredOrders.length})
                  </h2>
                  <p className="text-[10px] text-[#C4B6A6]">Filter by workflow status or payment</p>
                </div>
                <button 
                  onClick={() => {
                    setOrderToEdit(null);
                    setShowOrderModal(true);
                  }}
                  className="bg-[#D4AF37] text-[#160408] text-xs font-bold px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-[#F5D77F] transition shadow"
                >
                  <Plus className="w-3.5 h-3.5" /> Add Order
                </button>
              </div>

              {/* Filters Scrollable bar */}
              <div className="flex gap-1.5 overflow-x-auto pb-1 no-scrollbar text-[11px]">
                {[
                  { id: 'ALL', label: 'All' },
                  { id: 'ACTIVE', label: 'Active' },
                  { id: 'URGENT', label: 'Urgent 🔥' },
                  { id: 'PENDING_PAYMENT', label: 'Due Payment' },
                  ...STATUS_STEPS.map(s => ({ id: s, label: s }))
                ].map(f => (
                  <button
                    key={f.id}
                    onClick={() => setOrderFilter(f.id)}
                    className={`px-3 py-1 rounded-full whitespace-nowrap font-medium border transition ${
                      orderFilter === f.id
                        ? 'bg-[#D4AF37] text-[#160408] border-[#D4AF37] font-bold shadow'
                        : 'bg-[#2E0B13] text-[#C4B6A6] border-[#D4AF37]/30 hover:border-[#D4AF37]/60'
                    }`}
                  >
                    {f.label}
                  </button>
                ))}
              </div>

              {/* Orders List */}
              <div className="space-y-2.5">
                {filteredOrders.map(order => (
                  <OrderCard 
                    key={order.id} 
                    order={order} 
                    currency={settings.currency} 
                    onView={() => setSelectedOrder(order)} 
                  />
                ))}

                {filteredOrders.length === 0 && (
                  <div className="text-center py-10 bg-[#3A0F1A] rounded-xl border border-[#D4AF37]/20 p-6 space-y-2">
                    <Scissors className="w-10 h-10 text-[#D4AF37]/40 mx-auto" />
                    <h3 className="text-sm font-bold text-[#F8F1E5]">No Orders Found</h3>
                    <p className="text-xs text-[#C4B6A6]">No orders match the selected filter or search keyword.</p>
                    <button 
                      onClick={() => { setOrderFilter('ALL'); setSearchQuery(''); }}
                      className="text-xs text-[#D4AF37] underline"
                    >
                      Clear Filters
                    </button>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* TAB 3: CLIENT DIRECTORY */}
          {currentTab === 'clients' && (
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <div>
                  <h2 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">
                    Client Directory ({clients.length})
                  </h2>
                  <p className="text-[10px] text-[#C4B6A6]">Client profiles & saved measurements</p>
                </div>
                <button 
                  onClick={() => {
                    setClientToEdit(null);
                    setShowClientModal(true);
                  }}
                  className="bg-[#D4AF37] text-[#160408] text-xs font-bold px-3 py-1.5 rounded-lg flex items-center gap-1 hover:bg-[#F5D77F] transition shadow"
                >
                  <Plus className="w-3.5 h-3.5" /> Add Client
                </button>
              </div>

              <div className="space-y-2.5">
                {clients.filter(c => 
                  c.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                  c.phone.includes(searchQuery) ||
                  (c.address || '').toLowerCase().includes(searchQuery.toLowerCase())
                ).map(client => {
                  const clientOrders = orders.filter(o => o.customerPhone === client.phone || o.customerName === client.name);
                  return (
                    <div key={client.id} className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3.5 space-y-2.5">
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-serif font-bold text-sm text-[#F8F1E5]">{client.name}</h3>
                          <p className="text-[11px] text-[#C4B6A6] flex items-center gap-1 mt-0.5">
                            <Phone className="w-3 h-3 text-[#D4AF37]" /> {client.phone}
                          </p>
                          {client.address && (
                            <p className="text-[11px] text-[#C4B6A6] flex items-center gap-1 mt-0.5">
                              <MapPin className="w-3 h-3 text-[#D4AF37]" /> {client.address}
                            </p>
                          )}
                        </div>

                        <div className="flex items-center gap-1">
                          <a 
                            href={`tel:${client.phone}`} 
                            className="p-1.5 rounded-lg bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F5D77F] hover:bg-[#521524] transition"
                            title="Call"
                          >
                            <Phone className="w-3.5 h-3.5" />
                          </a>
                          <button 
                            onClick={() => {
                              setClientToEdit(client);
                              setShowClientModal(true);
                            }}
                            className="p-1.5 rounded-lg bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F5D77F] hover:bg-[#521524] transition"
                            title="Edit"
                          >
                            <Edit3 className="w-3.5 h-3.5" />
                          </button>
                          <button 
                            onClick={() => {
                              if (confirm(`Delete client "${client.name}"?`)) {
                                setClients(clients.filter(c => c.id !== client.id));
                                showToast("Client deleted!");
                              }
                            }}
                            className="p-1.5 rounded-lg bg-[#2E0B13] border border-red-500/40 text-red-400 hover:bg-red-950 transition"
                            title="Delete"
                          >
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        </div>
                      </div>

                      {/* Saved Measurements Box */}
                      <div className="bg-[#160408] p-2 rounded-lg border border-[#D4AF37]/20 space-y-1">
                        <span className="text-[9px] font-bold text-[#D4AF37] uppercase tracking-wider block">
                          Saved Measurements ({client.unit || 'in'})
                        </span>
                        <div className="grid grid-cols-4 gap-1.5 text-[10px] text-center">
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Chest</span>
                            <span className="font-bold text-[#F8F1E5]">{client.chest || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Waist</span>
                            <span className="font-bold text-[#F8F1E5]">{client.waist || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Hips</span>
                            <span className="font-bold text-[#F8F1E5]">{client.hips || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Shoulder</span>
                            <span className="font-bold text-[#F8F1E5]">{client.shoulder || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Sleeve</span>
                            <span className="font-bold text-[#F8F1E5]">{client.sleeve || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Trouser</span>
                            <span className="font-bold text-[#F8F1E5]">{client.trouserLength || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Neck</span>
                            <span className="font-bold text-[#F8F1E5]">{client.neck || '-'}</span>
                          </div>
                          <div className="bg-[#2E0B13] p-1 rounded border border-[#D4AF37]/10">
                            <span className="text-[#C4B6A6] block text-[8px]">Armhole</span>
                            <span className="font-bold text-[#F8F1E5]">{client.armhole || '-'}</span>
                          </div>
                        </div>

                        {client.notes && (
                          <p className="text-[10px] text-[#C4B6A6] italic mt-1 border-t border-[#D4AF37]/10 pt-1">
                            Note: {client.notes}
                          </p>
                        )}
                      </div>

                      {/* Quick Action: Create Order with this client */}
                      <div className="flex justify-between items-center pt-1 border-t border-[#D4AF37]/10">
                        <span className="text-[10px] text-[#C4B6A6]">
                          {clientOrders.length} Order(s) logged
                        </span>
                        <button
                          onClick={() => {
                            setOrderToEdit({
                              customerName: client.name,
                              customerPhone: client.phone,
                              customerAddress: client.address || '',
                              measurements: {
                                unit: client.unit || 'in',
                                chest: client.chest || '',
                                waist: client.waist || '',
                                hips: client.hips || '',
                                shoulder: client.shoulder || '',
                                sleeve: client.sleeve || '',
                                trouserLength: client.trouserLength || '',
                                neck: client.neck || '',
                                inseam: client.inseam || '',
                                armhole: client.armhole || '',
                                notes: client.notes || ''
                              }
                            });
                            setShowOrderModal(true);
                          }}
                          className="text-[11px] text-[#F5D77F] font-bold flex items-center gap-1 hover:underline"
                        >
                          <Plus className="w-3 h-3" /> New Order for Client
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}

          {/* TAB 4: FINANCIAL ACCOUNTING */}
          {currentTab === 'finances' && (
            <div className="space-y-3.5">
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-4 space-y-3.5">
                <div className="flex justify-between items-center">
                  <h2 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">Atelier Accounting Ledger</h2>
                  <span className="text-[10px] text-[#C4B6A6]">Live Balance</span>
                </div>

                <div className="grid grid-cols-3 gap-2 text-center">
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#D4AF37]/20">
                    <span className="text-[10px] text-[#C4B6A6] block">Total Billed</span>
                    <span className="text-sm font-bold text-[#F8F1E5]">{settings.currency}{totalRevenue.toLocaleString()}</span>
                  </div>
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#10B981]/30">
                    <span className="text-[10px] text-[#C4B6A6] block">Advance Recv.</span>
                    <span className="text-sm font-bold text-[#10B981]">{settings.currency}{totalCollected.toLocaleString()}</span>
                  </div>
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#F59E0B]/30">
                    <span className="text-[10px] text-[#C4B6A6] block">Total Pending</span>
                    <span className="text-sm font-bold text-[#F59E0B]">{settings.currency}{totalPending.toLocaleString()}</span>
                  </div>
                </div>

                {/* Status counts */}
                <div className="grid grid-cols-3 gap-2 text-center text-[10px] pt-1">
                  <div className="bg-[#2E0B13] p-1.5 rounded-lg border border-[#10B981]/20 text-[#10B981]">
                    <b>{fullyPaidCount}</b> Fully Paid
                  </div>
                  <div className="bg-[#2E0B13] p-1.5 rounded-lg border border-[#F59E0B]/20 text-[#F59E0B]">
                    <b>{partialPaidCount}</b> Partial
                  </div>
                  <div className="bg-[#2E0B13] p-1.5 rounded-lg border border-red-500/20 text-red-400">
                    <b>{unpaidCount}</b> Unpaid
                  </div>
                </div>
              </div>

              {/* Outstanding Dues Ledger */}
              <div className="space-y-2">
                <div className="flex justify-between items-center">
                  <h3 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider flex items-center gap-1.5">
                    <AlertCircle className="w-3.5 h-3.5 text-[#F59E0B]" /> Outstanding Dues Ledger
                  </h3>
                  <span className="text-[10px] text-[#F59E0B] font-bold">
                    {orders.filter(o => (o.totalAmount - o.receivedAmount) > 0).length} Orders Pending
                  </span>
                </div>

                {orders.filter(o => (o.totalAmount - o.receivedAmount) > 0).map(order => {
                  const pending = order.totalAmount - order.receivedAmount;
                  return (
                    <div 
                      key={order.id} 
                      className="bg-[#3A0F1A] border border-[#F59E0B]/40 rounded-xl p-3 space-y-2"
                    >
                      <div className="flex justify-between items-start">
                        <div>
                          <div className="flex items-center gap-2">
                            <span className="bg-[#160408] border border-[#D4AF37]/40 text-[#F5D77F] text-[10px] font-mono px-1.5 py-0.5 rounded font-bold">
                              {order.orderNumber}
                            </span>
                            <h4 className="text-xs font-bold text-[#F8F1E5]">{order.customerName}</h4>
                          </div>
                          <p className="text-[10px] text-[#C4B6A6] mt-0.5">{order.suitType}</p>
                        </div>
                        <span className="text-xs font-bold text-[#F59E0B]">
                          Due: {settings.currency}{pending}
                        </span>
                      </div>

                      <div className="flex justify-between items-center pt-2 border-t border-[#D4AF37]/20">
                        <a 
                          href={`tel:${order.customerPhone}`}
                          className="text-[10px] text-[#F5D77F] flex items-center gap-1 bg-[#2E0B13] px-2 py-1 rounded-lg border border-[#D4AF37]/30"
                        >
                          <Phone className="w-3 h-3" /> Call Client
                        </a>

                        <button 
                          onClick={() => {
                            setSelectedOrder(order);
                            setPaymentAmountInput(pending.toString());
                            setShowRecordPaymentModal(true);
                          }}
                          className="bg-[#10B981] hover:bg-emerald-400 text-[#160408] text-[11px] font-bold px-3 py-1 rounded-lg transition shadow"
                        >
                          + Record Payment
                        </button>
                      </div>
                    </div>
                  );
                })}

                {orders.filter(o => (o.totalAmount - o.receivedAmount) > 0).length === 0 && (
                  <div className="text-center py-8 bg-[#3A0F1A] rounded-xl border border-[#10B981]/30 p-4">
                    <CheckCircle2 className="w-8 h-8 text-[#10B981] mx-auto mb-1" />
                    <h4 className="text-xs font-bold text-[#10B981]">All Customer Dues Are Cleared!</h4>
                    <p className="text-[10px] text-[#C4B6A6]">No outstanding balances currently.</p>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* TAB 5: SETTINGS & CUSTOMIZATION */}
          {currentTab === 'settings' && (
            <div className="space-y-3.5">
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-4 space-y-3.5">
                <h2 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">Atelier Branding & Profile</h2>
                
                <div>
                  <label className="text-[11px] text-[#C4B6A6] block mb-1 font-semibold">Boutique / Atelier Name</label>
                  <input 
                    type="text" 
                    value={settings.boutiqueName} 
                    onChange={(e) => setSettings(prev => ({ ...prev, boutiqueName: e.target.value }))}
                    className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg px-3 py-2 text-xs text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                  />
                </div>

                <div>
                  <label className="text-[11px] text-[#C4B6A6] block mb-1 font-semibold">Tagline / Subtitle</label>
                  <input 
                    type="text" 
                    value={settings.tagline} 
                    onChange={(e) => setSettings(prev => ({ ...prev, tagline: e.target.value }))}
                    className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg px-3 py-2 text-xs text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                  />
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-[11px] text-[#C4B6A6] block mb-1 font-semibold">Official Phone</label>
                    <input 
                      type="text" 
                      value={settings.phone || ''} 
                      onChange={(e) => setSettings(prev => ({ ...prev, phone: e.target.value }))}
                      className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg px-3 py-2 text-xs text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                    />
                  </div>
                  <div>
                    <label className="text-[11px] text-[#C4B6A6] block mb-1 font-semibold">Currency</label>
                    <select 
                      value={settings.currency} 
                      onChange={(e) => setSettings(prev => ({ ...prev, currency: e.target.value }))}
                      className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg px-3 py-2 text-xs text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                    >
                      {['₹', '$', 'AED', 'SAR', '£', '€', 'Rs.'].map(c => (
                        <option key={c} value={c}>{c}</option>
                      ))}
                    </select>
                  </div>
                </div>

                {/* Logo picker */}
                <div>
                  <label className="text-[11px] text-[#C4B6A6] block mb-1.5 font-semibold">Boutique Logo / Emblem</label>
                  <div className="flex items-center gap-3">
                    <div className="w-14 h-14 rounded-full border-2 border-[#D4AF37] bg-[#160408] flex items-center justify-center overflow-hidden flex-shrink-0 text-2xl shadow-inner">
                      {settings.customLogoUrl ? (
                        <img src={settings.customLogoUrl} alt="Logo" className="w-full h-full object-cover" />
                      ) : (
                        settings.selectedLogo
                      )}
                    </div>
                    <div className="flex-1 space-y-1.5">
                      <label className="flex items-center justify-center gap-1.5 bg-[#D4AF37] text-[#160408] font-bold text-xs py-2 px-3 rounded-lg cursor-pointer hover:bg-[#F5D77F] transition shadow">
                        <ImageIcon className="w-4 h-4" /> Pick Custom Image
                        <input type="file" accept="image/*" className="hidden" onChange={handleLogoUpload} />
                      </label>
                      {settings.customLogoUrl && (
                        <button 
                          onClick={() => {
                            setSettings(prev => ({ ...prev, customLogoUrl: null }));
                            showToast("Custom logo removed!");
                          }}
                          className="w-full bg-[#160408] border border-[#D4AF37]/30 text-[10px] py-1 rounded text-[#C4B6A6] hover:text-white"
                        >
                          Revert to Preset Icon
                        </button>
                      )}
                    </div>
                  </div>

                  <div className="flex gap-1.5 mt-2.5">
                    {['✂️', '🪡', '👑', '👗', '💎', '🧵', '🥻', '✨'].map(ico => (
                      <button 
                        key={ico}
                        onClick={() => {
                          setSettings(prev => ({ ...prev, selectedLogo: ico, customLogoUrl: null }));
                          showToast(`Emblem set to ${ico}`);
                        }}
                        className={`flex-1 py-1.5 rounded-lg text-sm border transition ${
                          settings.selectedLogo === ico && !settings.customLogoUrl ? 'bg-[#521524] border-[#D4AF37] font-bold ring-1 ring-[#D4AF37]' : 'bg-[#160408] border-[#D4AF37]/20'
                        }`}
                      >
                        {ico}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              {/* Data Backup & Restore */}
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-4 space-y-3">
                <h3 className="text-xs font-bold text-[#F5D77F] uppercase tracking-wider">Data Backup & Export</h3>
                <p className="text-[11px] text-[#C4B6A6]">Download your entire database or restore from a previous JSON backup.</p>
                <div className="grid grid-cols-2 gap-2">
                  <button 
                    onClick={handleExportBackup}
                    className="bg-[#2E0B13] border border-[#D4AF37]/50 text-[#F5D77F] font-bold text-xs py-2 rounded-lg flex items-center justify-center gap-1.5 hover:bg-[#521524] transition"
                  >
                    <Download className="w-3.5 h-3.5" /> Export Backup
                  </button>
                  <label className="bg-[#2E0B13] border border-[#D4AF37]/50 text-[#F5D77F] font-bold text-xs py-2 rounded-lg flex items-center justify-center gap-1.5 hover:bg-[#521524] transition cursor-pointer">
                    <Upload className="w-3.5 h-3.5" /> Import Backup
                    <input type="file" accept=".json" className="hidden" onChange={handleImportBackup} />
                  </label>
                </div>
              </div>

              {/* Creator Credit Box */}
              <div className="rounded-xl p-4 bg-gradient-to-b from-[#2E0B13] to-[#160408] border-2 border-[#D4AF37] text-center space-y-1 shadow-lg">
                <div className="flex items-center justify-center gap-1 text-[#F5D77F] text-[10px] font-bold tracking-widest uppercase">
                  <Sparkles className="w-3.5 h-3.5 text-[#D4AF37]" /> Bespoke Tailoring Suite
                </div>
                <h3 className="text-base font-serif font-bold text-[#F5D77F]">
                  This app created by Amir Khan
                </h3>
                <p className="text-[11px] text-[#C4B6A6]">Designed exclusively for Aysha Boutique Atelier</p>
              </div>
            </div>
          )}

        </main>

        {/* Bottom Navigation Bar */}
        <nav className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-md bg-[#22070D] border-t border-[#D4AF37]/40 py-2 px-2 flex justify-around items-center z-50 shadow-2xl backdrop-blur-md">
          {[
            { id: 'dashboard', label: 'Atelier', icon: Scissors },
            { id: 'orders', label: 'Orders', icon: ShoppingBag },
            { id: 'clients', label: 'Clients', icon: Users },
            { id: 'finances', label: 'Finances', icon: DollarSign },
            { id: 'settings', label: 'Settings', icon: Settings }
          ].map(tab => {
            const Icon = tab.icon;
            const isActive = currentTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setCurrentTab(tab.id)}
                className={`flex flex-col items-center gap-0.5 py-1 px-3 rounded-xl transition ${
                  isActive ? 'bg-[#D4AF37] text-[#160408] font-bold shadow-lg scale-105' : 'text-[#C4B6A6] hover:text-[#F8F1E5]'
                }`}
              >
                <Icon className="w-4 h-4" />
                <span className="text-[9px]">{tab.label}</span>
              </button>
            );
          })}
        </nav>

        {/* MODAL 1: ORDER CREATE / EDIT (FULL 3-STEP SUITE) */}
        {showOrderModal && (
          <OrderFormModal 
            orderToEdit={orderToEdit}
            clients={clients}
            currency={settings.currency}
            onClose={() => setShowOrderModal(false)}
            onSave={(savedOrder) => {
              if (orderToEdit && orderToEdit.id) {
                setOrders(orders.map(o => o.id === orderToEdit.id ? savedOrder : o));
                showToast("Order updated successfully!");
              } else {
                setOrders([savedOrder, ...orders]);
                showToast("New order added successfully!");
              }
              setShowOrderModal(false);
            }}
          />
        )}

        {/* MODAL 2: CLIENT CREATE / EDIT */}
        {showClientModal && (
          <ClientFormModal 
            clientToEdit={clientToEdit}
            onClose={() => setShowClientModal(false)}
            onSave={(savedClient) => {
              if (clientToEdit && clientToEdit.id) {
                setClients(clients.map(c => c.id === clientToEdit.id ? savedClient : c));
                showToast("Client updated successfully!");
              } else {
                setClients([savedClient, ...clients]);
                showToast("New client saved!");
              }
              setShowClientModal(false);
            }}
          />
        )}

        {/* MODAL 3: ORDER INVOICE & DETAILS */}
        {selectedOrder && (
          <Modal title={`Invoice: ${selectedOrder.orderNumber}`} onClose={() => setSelectedOrder(null)}>
            <div className="space-y-3.5 text-xs">
              
              {/* Header Info */}
              <div className="flex justify-between items-start border-b border-[#D4AF37]/30 pb-2.5">
                <div>
                  <h3 className="font-serif font-bold text-sm text-[#F5D77F]">{selectedOrder.suitType}</h3>
                  <p className="text-[#F8F1E5] font-semibold">{selectedOrder.customerName}</p>
                  <p className="text-[#C4B6A6]">{selectedOrder.customerPhone} • {selectedOrder.customerAddress || 'No Address'}</p>
                  <p className="text-[11px] text-[#D4AF37] mt-0.5">
                    Taken: <b>{selectedOrder.dateTaken}</b> | Due: <b>{selectedOrder.dateDueForDelivery}</b>
                  </p>
                </div>
                <span className="bg-[#521524] text-[#F5D77F] border border-[#D4AF37]/50 px-2.5 py-1 rounded-full text-[10px] font-bold">
                  {selectedOrder.status}
                </span>
              </div>

              {/* Status Stepper */}
              <div>
                <span className="text-[10px] font-bold text-[#F5D77F] block mb-1 uppercase tracking-wider">
                  Update Workflow Status
                </span>
                <div className="grid grid-cols-3 gap-1 text-center">
                  {STATUS_STEPS.map(st => {
                    const isCurrent = selectedOrder.status === st;
                    return (
                      <button
                        key={st}
                        onClick={() => {
                          const updated = orders.map(o => o.id === selectedOrder.id ? { ...o, status: st } : o);
                          setOrders(updated);
                          setSelectedOrder({ ...selectedOrder, status: st });
                          showToast(`Status updated to ${st}`);
                        }}
                        className={`text-[10px] py-1.5 rounded border transition ${
                          isCurrent 
                            ? 'bg-[#D4AF37] text-[#160408] font-bold border-[#D4AF37] shadow' 
                            : 'bg-[#160408] text-[#C4B6A6] border-[#D4AF37]/20 hover:border-[#D4AF37]/50'
                        }`}
                      >
                        {st}
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Measurements Card */}
              {selectedOrder.measurements && (
                <div className="bg-[#160408] p-2.5 rounded-xl border border-[#D4AF37]/30 space-y-1.5">
                  <div className="flex justify-between items-center">
                    <span className="text-[10px] font-bold text-[#D4AF37] uppercase tracking-wider">
                      Measurements ({selectedOrder.measurements.unit || 'in'})
                    </span>
                    <span className="text-[10px] text-[#C4B6A6]">Size: {selectedOrder.standardSize || 'Custom'}</span>
                  </div>
                  <div className="grid grid-cols-4 gap-1 text-center text-[10px]">
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Chest</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.chest || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Waist</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.waist || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Hips</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.hips || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Shoulder</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.shoulder || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Sleeve</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.sleeve || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Trouser</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.trouserLength || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Neck</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.neck || '-'}</span>
                    </div>
                    <div className="bg-[#2E0B13] p-1 rounded">
                      <span className="text-[#C4B6A6] block text-[8px]">Armhole</span>
                      <span className="font-bold text-[#F8F1E5]">{selectedOrder.measurements.armhole || '-'}</span>
                    </div>
                  </div>
                  {selectedOrder.specialInstructions && (
                    <p className="text-[10px] text-[#C4B6A6] border-t border-[#D4AF37]/10 pt-1">
                      <b>Instructions:</b> {selectedOrder.specialInstructions}
                    </p>
                  )}
                </div>
              )}

              {/* Financial Ledger Breakdown */}
              <div className="bg-[#160408] p-3 rounded-xl border border-[#D4AF37]/30 space-y-1.5">
                <div className="flex justify-between">
                  <span className="text-[#C4B6A6]">Total Billed:</span>
                  <span className="font-bold text-[#F8F1E5]">{settings.currency}{selectedOrder.totalAmount}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-[#C4B6A6]">Advance Paid:</span>
                  <span className="font-bold text-[#10B981]">{settings.currency}{selectedOrder.receivedAmount}</span>
                </div>
                <div className="flex justify-between border-t border-[#D4AF37]/20 pt-1">
                  <span className="text-[#C4B6A6]">Pending Balance:</span>
                  <span className="font-bold text-[#F59E0B]">
                    {settings.currency}{selectedOrder.totalAmount - selectedOrder.receivedAmount}
                  </span>
                </div>
              </div>

              {/* Actions Grid */}
              <div className="grid grid-cols-3 gap-1.5">
                <a 
                  href={`tel:${selectedOrder.customerPhone}`}
                  className="bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F5D77F] py-2 rounded-lg text-xs font-bold flex items-center justify-center gap-1 hover:bg-[#3A0F1A]"
                >
                  <Phone className="w-3.5 h-3.5" /> Call
                </a>
                
                <button 
                  onClick={() => {
                    const text = generateReceiptText(selectedOrder);
                    if (navigator.clipboard) {
                      navigator.clipboard.writeText(text);
                      showToast("Receipt copied! Ready to send on WhatsApp.");
                    } else {
                      alert(text);
                    }
                  }}
                  className="bg-[#D4AF37] text-[#160408] py-2 rounded-lg text-xs font-bold flex items-center justify-center gap-1 hover:bg-[#F5D77F]"
                >
                  <Share2 className="w-3.5 h-3.5" /> Share
                </button>

                <button 
                  onClick={() => {
                    setOrderToEdit(selectedOrder);
                    setSelectedOrder(null);
                    setShowOrderModal(true);
                  }}
                  className="bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F8F1E5] py-2 rounded-lg text-xs font-bold flex items-center justify-center gap-1 hover:bg-[#3A0F1A]"
                >
                  <Edit3 className="w-3.5 h-3.5" /> Edit
                </button>
              </div>

              {/* Payment Recording / Delete button */}
              <div className="flex gap-2">
                <button
                  onClick={() => {
                    setPaymentAmountInput((selectedOrder.totalAmount - selectedOrder.receivedAmount).toString());
                    setShowRecordPaymentModal(true);
                  }}
                  className="flex-1 bg-[#10B981] hover:bg-emerald-400 text-[#160408] py-2 rounded-lg text-xs font-bold flex items-center justify-center gap-1 shadow"
                >
                  <DollarSign className="w-3.5 h-3.5" /> Add Payment
                </button>

                <button
                  onClick={() => {
                    if (confirm(`Delete Order ${selectedOrder.orderNumber}?`)) {
                      setOrders(orders.filter(o => o.id !== selectedOrder.id));
                      setSelectedOrder(null);
                      showToast("Order deleted!");
                    }
                  }}
                  className="bg-red-900/40 border border-red-500/40 text-red-300 px-3 py-2 rounded-lg text-xs font-bold hover:bg-red-900"
                  title="Delete Order"
                >
                  <Trash2 className="w-3.5 h-3.5" />
                </button>
              </div>

            </div>
          </Modal>
        )}

        {/* MODAL 4: RECORD PAYMENT MODAL */}
        {showRecordPaymentModal && selectedOrder && (
          <Modal title={`Record Payment: ${selectedOrder.orderNumber}`} onClose={() => setShowRecordPaymentModal(false)}>
            <form onSubmit={(e) => {
              e.preventDefault();
              const added = Number(paymentAmountInput || 0);
              if (added <= 0) {
                alert("Please enter a valid payment amount!");
                return;
              }
              const newReceived = Number(selectedOrder.receivedAmount || 0) + added;
              const updated = orders.map(o => o.id === selectedOrder.id ? { ...o, receivedAmount: newReceived } : o);
              setOrders(updated);
              setSelectedOrder({ ...selectedOrder, receivedAmount: newReceived });
              setShowRecordPaymentModal(false);
              showToast(`Payment of ${settings.currency}${added} recorded!`);
            }} className="space-y-3 text-xs">
              <div className="bg-[#160408] p-3 rounded-lg border border-[#D4AF37]/30 space-y-1">
                <p className="text-[#C4B6A6]">Customer: <b className="text-white">{selectedOrder.customerName}</b></p>
                <p className="text-[#C4B6A6]">Total Billed: <b className="text-white">{settings.currency}{selectedOrder.totalAmount}</b></p>
                <p className="text-[#C4B6A6]">Already Paid: <b className="text-[#10B981]">{settings.currency}{selectedOrder.receivedAmount}</b></p>
                <p className="text-[#F59E0B] font-bold">Remaining Due: {settings.currency}{selectedOrder.totalAmount - selectedOrder.receivedAmount}</p>
              </div>

              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">
                  Payment Amount Received ({settings.currency}) *
                </label>
                <input 
                  required
                  type="number"
                  step="any"
                  value={paymentAmountInput}
                  onChange={(e) => setPaymentAmountInput(e.target.value)}
                  placeholder="Enter amount"
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] text-sm font-bold focus:outline-none focus:border-[#D4AF37]"
                />
              </div>

              <button 
                type="submit" 
                className="w-full bg-[#10B981] hover:bg-emerald-400 text-[#160408] font-bold py-2.5 rounded-lg text-xs transition shadow"
              >
                Confirm & Update Ledger
              </button>
            </form>
          </Modal>
        )}

      </div>
    </div>
  );
}

// -------------------------------------------------------------
// SUB-COMPONENTS & DIALOGS
// -------------------------------------------------------------

function OrderCard({ order, currency, onView }) {
  const pending = Number(order.totalAmount || 0) - Number(order.receivedAmount || 0);
  return (
    <div 
      onClick={onView}
      className="bg-[#3A0F1A] border border-[#D4AF37]/30 hover:border-[#D4AF37] rounded-xl p-3.5 space-y-2 cursor-pointer transition shadow hover:shadow-lg"
    >
      <div className="flex justify-between items-center">
        <span className="bg-[#160408] border border-[#D4AF37]/50 text-[#F5D77F] text-[10px] font-mono px-2 py-0.5 rounded font-bold">
          {order.orderNumber}
        </span>
        <span className="text-[10px] bg-[#521524] text-[#F5D77F] px-2.5 py-0.5 rounded-full font-bold border border-[#D4AF37]/40">
          Due: {order.dateDueForDelivery}
        </span>
      </div>

      <div>
        <h3 className="font-serif font-bold text-xs text-[#F8F1E5]">{order.suitType}</h3>
        <p className="text-[11px] text-[#C4B6A6] mt-0.5">👤 {order.customerName} • {order.customerPhone}</p>
      </div>

      <div className="bg-[#160408] p-2 rounded-lg border border-[#D4AF37]/20 flex justify-between items-center text-[10px]">
        <span>Total: <b className="text-white">{currency}{order.totalAmount}</b></span>
        <span>Paid: <b className="text-[#10B981]">{currency}{order.receivedAmount}</b></span>
        <span>Due: <b className={pending > 0 ? 'text-[#F59E0B]' : 'text-[#10B981]'}>{currency}{pending}</b></span>
      </div>
    </div>
  );
}

function Modal({ title, onClose, children }) {
  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-[#2E0B13] border-2 border-[#D4AF37] rounded-2xl w-full max-w-sm max-h-[92vh] overflow-y-auto p-4 relative shadow-2xl">
        <div className="flex justify-between items-center border-b border-[#D4AF37]/30 pb-2.5 mb-3">
          <h3 className="font-serif font-bold text-sm text-[#F5D77F] uppercase tracking-wider">{title}</h3>
          <button onClick={onClose} className="text-[#C4B6A6] hover:text-[#F8F1E5] p-1">
            <X className="w-4 h-4" />
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

function OrderFormModal({ orderToEdit, clients, currency, onClose, onSave }) {
  const [tab, setTab] = useState(0); // 0: Specs, 1: Measurements, 2: Finances

  // Form State
  const [customerName, setCustomerName] = useState(orderToEdit?.customerName || '');
  const [customerPhone, setCustomerPhone] = useState(orderToEdit?.customerPhone || '');
  const [customerAddress, setCustomerAddress] = useState(orderToEdit?.customerAddress || '');
  const [suitType, setSuitType] = useState(orderToEdit?.suitType || GARMENT_TYPES[0]);
  const [numberOfSuits, setNumberOfSuits] = useState(orderToEdit?.numberOfSuits || 1);
  const [standardSize, setStandardSize] = useState(orderToEdit?.standardSize || 'Custom');
  const [fabricDetails, setFabricDetails] = useState(orderToEdit?.fabricDetails || '');
  const [specialInstructions, setSpecialInstructions] = useState(orderToEdit?.specialInstructions || '');

  // Measurements
  const [unit, setUnit] = useState(orderToEdit?.measurements?.unit || 'in');
  const [chest, setChest] = useState(orderToEdit?.measurements?.chest || '');
  const [waist, setWaist] = useState(orderToEdit?.measurements?.waist || '');
  const [hips, setHips] = useState(orderToEdit?.measurements?.hips || '');
  const [shoulder, setShoulder] = useState(orderToEdit?.measurements?.shoulder || '');
  const [sleeve, setSleeve] = useState(orderToEdit?.measurements?.sleeve || '');
  const [trouserLength, setTrouserLength] = useState(orderToEdit?.measurements?.trouserLength || '');
  const [neck, setNeck] = useState(orderToEdit?.measurements?.neck || '');
  const [inseam, setInseam] = useState(orderToEdit?.measurements?.inseam || '');
  const [armhole, setArmhole] = useState(orderToEdit?.measurements?.armhole || '');

  // Finances
  const [totalAmount, setTotalAmount] = useState(orderToEdit?.totalAmount || '');
  const [receivedAmount, setReceivedAmount] = useState(orderToEdit?.receivedAmount || '');
  const [dateDueForDelivery, setDateDueForDelivery] = useState(
    orderToEdit?.dateDueForDelivery || new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
  );
  const [status, setStatus] = useState(orderToEdit?.status || 'Order Taken');

  // Customer dropdown selection
  const handleSelectClient = (client) => {
    setCustomerName(client.name);
    setCustomerPhone(client.phone);
    setCustomerAddress(client.address || '');
    if (client.chest) setChest(client.chest);
    if (client.waist) setWaist(client.waist);
    if (client.hips) setHips(client.hips);
    if (client.shoulder) setShoulder(client.shoulder);
    if (client.sleeve) setSleeve(client.sleeve);
    if (client.trouserLength) setTrouserLength(client.trouserLength);
    if (client.neck) setNeck(client.neck);
    if (client.inseam) setInseam(client.inseam);
    if (client.armhole) setArmhole(client.armhole);
    if (client.unit) setUnit(client.unit);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!customerName || !customerPhone || !totalAmount) {
      alert("Please fill customer name, phone number, and total bill amount!");
      return;
    }
    const orderData = {
      id: orderToEdit?.id || Date.now(),
      orderNumber: orderToEdit?.orderNumber || 'AY-' + Math.floor(1000 + Math.random() * 9000),
      customerName,
      customerPhone,
      customerAddress,
      suitType,
      numberOfSuits: Number(numberOfSuits || 1),
      standardSize,
      fabricDetails,
      specialInstructions,
      dateTaken: orderToEdit?.dateTaken || new Date().toISOString().split('T')[0],
      dateDueForDelivery,
      status,
      totalAmount: Number(totalAmount || 0),
      receivedAmount: Number(receivedAmount || 0),
      measurements: {
        unit,
        chest,
        waist,
        hips,
        shoulder,
        sleeve,
        trouserLength,
        neck,
        inseam,
        armhole
      }
    };
    onSave(orderData);
  };

  return (
    <Modal title={orderToEdit?.id ? `Edit ${orderToEdit.orderNumber}` : "New Atelier Order"} onClose={onClose}>
      {/* 3 Step Tabs */}
      <div className="flex border-b border-[#D4AF37]/30 mb-3 text-[11px]">
        {[
          { idx: 0, label: '1. Specs' },
          { idx: 1, label: '2. Measurements' },
          { idx: 2, label: '3. Accounts & Due' }
        ].map(t => (
          <button
            key={t.idx}
            type="button"
            onClick={() => setTab(t.idx)}
            className={`flex-1 py-1.5 font-bold border-b-2 transition ${
              tab === t.idx ? 'border-[#D4AF37] text-[#F5D77F]' : 'border-transparent text-[#C4B6A6]'
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      <form onSubmit={handleSubmit} className="space-y-3 text-xs">
        {/* STEP 1: SPECS */}
        {tab === 0 && (
          <div className="space-y-2.5">
            {clients.length > 0 && (
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-1">Auto-Fill from Existing Client:</label>
                <select 
                  onChange={(e) => {
                    const c = clients.find(cl => cl.id === Number(e.target.value));
                    if (c) handleSelectClient(c);
                  }}
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-[#F8F1E5] text-xs focus:outline-none"
                >
                  <option value="">-- Select Client --</option>
                  {clients.map(c => (
                    <option key={c.id} value={c.id}>{c.name} ({c.phone})</option>
                  ))}
                </select>
              </div>
            )}

            <div>
              <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Customer Full Name *</label>
              <input 
                required
                value={customerName}
                onChange={(e) => setCustomerName(e.target.value)}
                placeholder="e.g. Fatima Sheikh"
                className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
              />
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Phone Number *</label>
                <input 
                  required
                  value={customerPhone}
                  onChange={(e) => setCustomerPhone(e.target.value)}
                  placeholder="+91 "
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Address / City</label>
                <input 
                  value={customerAddress}
                  onChange={(e) => setCustomerAddress(e.target.value)}
                  placeholder="e.g. Civil Lines"
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
            </div>

            <div>
              <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Garment / Suit Style *</label>
              <select 
                value={suitType}
                onChange={(e) => setSuitType(e.target.value)}
                className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
              >
                {GARMENT_TYPES.map(g => (
                  <option key={g} value={g}>{g}</option>
                ))}
              </select>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">No. of Suits</label>
                <input 
                  type="number"
                  min="1"
                  value={numberOfSuits}
                  onChange={(e) => setNumberOfSuits(e.target.value)}
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Standard Size</label>
                <select 
                  value={standardSize}
                  onChange={(e) => setStandardSize(e.target.value)}
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                >
                  {STANDARD_SIZES.map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
            </div>

            <div>
              <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Fabric & Embroidery Details</label>
              <input 
                value={fabricDetails}
                onChange={(e) => setFabricDetails(e.target.value)}
                placeholder="e.g. Pure Raw Silk with Gold Zari"
                className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
              />
            </div>

            <button 
              type="button" 
              onClick={() => setTab(1)}
              className="w-full bg-[#D4AF37] text-[#160408] font-bold py-2 rounded-lg text-xs mt-2 flex items-center justify-center gap-1"
            >
              Next: Measurements <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
        )}

        {/* STEP 2: BODY MEASUREMENTS */}
        {tab === 1 && (
          <div className="space-y-2.5">
            <div className="flex justify-between items-center">
              <span className="text-[11px] text-[#F5D77F] font-bold uppercase">Precision Measurements</span>
              <div className="flex bg-[#160408] p-0.5 rounded-lg border border-[#D4AF37]/30 text-[10px]">
                <button
                  type="button"
                  onClick={() => setUnit('in')}
                  className={`px-2 py-0.5 rounded ${unit === 'in' ? 'bg-[#D4AF37] text-[#160408] font-bold' : 'text-[#C4B6A6]'}`}
                >
                  Inches (in)
                </button>
                <button
                  type="button"
                  onClick={() => setUnit('cm')}
                  className={`px-2 py-0.5 rounded ${unit === 'cm' ? 'bg-[#D4AF37] text-[#160408] font-bold' : 'text-[#C4B6A6]'}`}
                >
                  Centimeters (cm)
                </button>
              </div>
            </div>

            <div className="grid grid-cols-3 gap-2">
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Chest</label>
                <input value={chest} onChange={(e) => setChest(e.target.value)} placeholder="36" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Waist</label>
                <input value={waist} onChange={(e) => setWaist(e.target.value)} placeholder="30" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Hips</label>
                <input value={hips} onChange={(e) => setHips(e.target.value)} placeholder="38" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Shoulder</label>
                <input value={shoulder} onChange={(e) => setShoulder(e.target.value)} placeholder="14.5" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Sleeve</label>
                <input value={sleeve} onChange={(e) => setSleeve(e.target.value)} placeholder="18" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Trouser L.</label>
                <input value={trouserLength} onChange={(e) => setTrouserLength(e.target.value)} placeholder="42" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Neck</label>
                <input value={neck} onChange={(e) => setNeck(e.target.value)} placeholder="7" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Inseam</label>
                <input value={inseam} onChange={(e) => setInseam(e.target.value)} placeholder="30" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
              <div>
                <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Armhole</label>
                <input value={armhole} onChange={(e) => setArmhole(e.target.value)} placeholder="16" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
              </div>
            </div>

            <div>
              <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Tailoring / Cut Instructions</label>
              <textarea 
                rows="2"
                value={specialInstructions}
                onChange={(e) => setSpecialInstructions(e.target.value)}
                placeholder="e.g. Deep back neckline, side slits, flare trouser"
                className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
              />
            </div>

            <div className="flex gap-2 pt-1">
              <button 
                type="button" 
                onClick={() => setTab(0)}
                className="flex-1 bg-[#160408] border border-[#D4AF37]/40 text-[#C4B6A6] font-bold py-2 rounded-lg text-xs"
              >
                Back
              </button>
              <button 
                type="button" 
                onClick={() => setTab(2)}
                className="flex-1 bg-[#D4AF37] text-[#160408] font-bold py-2 rounded-lg text-xs flex items-center justify-center gap-1"
              >
                Next: Accounts <ArrowRight className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        )}

        {/* STEP 3: FINANCIAL ACCOUNTING & DUE DATE */}
        {tab === 2 && (
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Total Bill ({currency}) *</label>
                <input 
                  required
                  type="number"
                  step="any"
                  value={totalAmount}
                  onChange={(e) => setTotalAmount(e.target.value)}
                  placeholder="3500"
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] font-bold focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Advance ({currency})</label>
                <input 
                  type="number"
                  step="any"
                  value={receivedAmount}
                  onChange={(e) => setReceivedAmount(e.target.value)}
                  placeholder="1500"
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] font-bold focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
            </div>

            {/* Live Balance Box */}
            <div className="bg-[#160408] p-2.5 rounded-lg border border-[#D4AF37]/30 flex justify-between items-center text-xs">
              <span className="text-[#C4B6A6]">Calculated Due Balance:</span>
              <span className="font-bold text-[#F59E0B]">
                {currency}{(Number(totalAmount || 0) - Number(receivedAmount || 0))}
              </span>
            </div>

            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Delivery Due Date *</label>
                <input 
                  required
                  type="date"
                  value={dateDueForDelivery}
                  onChange={(e) => setDateDueForDelivery(e.target.value)}
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                />
              </div>
              <div>
                <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Initial Status</label>
                <select 
                  value={status}
                  onChange={(e) => setStatus(e.target.value)}
                  className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
                >
                  {STATUS_STEPS.map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </div>
            </div>

            <button 
              type="submit" 
              className="w-full bg-[#D4AF37] hover:bg-[#F5D77F] text-[#160408] font-bold py-2.5 rounded-lg text-xs transition shadow-lg mt-2"
            >
              Save Order & Generate Receipt
            </button>
          </div>
        )}
      </form>
    </Modal>
  );
}

function ClientFormModal({ clientToEdit, onClose, onSave }) {
  const [name, setName] = useState(clientToEdit?.name || '');
  const [phone, setPhone] = useState(clientToEdit?.phone || '');
  const [address, setAddress] = useState(clientToEdit?.address || '');
  const [unit, setUnit] = useState(clientToEdit?.unit || 'in');
  const [chest, setChest] = useState(clientToEdit?.chest || '');
  const [waist, setWaist] = useState(clientToEdit?.waist || '');
  const [hips, setHips] = useState(clientToEdit?.hips || '');
  const [shoulder, setShoulder] = useState(clientToEdit?.shoulder || '');
  const [sleeve, setSleeve] = useState(clientToEdit?.sleeve || '');
  const [trouserLength, setTrouserLength] = useState(clientToEdit?.trouserLength || '');
  const [neck, setNeck] = useState(clientToEdit?.neck || '');
  const [inseam, setInseam] = useState(clientToEdit?.inseam || '');
  const [armhole, setArmhole] = useState(clientToEdit?.armhole || '');
  const [notes, setNotes] = useState(clientToEdit?.notes || '');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!name || !phone) {
      alert("Please enter client name and phone number!");
      return;
    }
    onSave({
      id: clientToEdit?.id || Date.now(),
      name,
      phone,
      address,
      unit,
      chest,
      waist,
      hips,
      shoulder,
      sleeve,
      trouserLength,
      neck,
      inseam,
      armhole,
      notes
    });
  };

  return (
    <Modal title={clientToEdit ? "Edit Client Profile" : "Add New Client"} onClose={onClose}>
      <form onSubmit={handleSubmit} className="space-y-3 text-xs">
        <div>
          <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Client Full Name *</label>
          <input 
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="e.g. Fatima Sheikh"
            className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
          />
        </div>

        <div className="grid grid-cols-2 gap-2">
          <div>
            <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Phone Number *</label>
            <input 
              required
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="+91 "
              className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
            />
          </div>
          <div>
            <label className="text-[11px] text-[#F5D77F] font-bold block mb-1">Address / City</label>
            <input 
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="e.g. Civil Lines"
              className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-2 text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37]"
            />
          </div>
        </div>

        <div className="border-t border-[#D4AF37]/20 pt-2 space-y-2">
          <div className="flex justify-between items-center">
            <span className="text-[11px] text-[#F5D77F] font-bold uppercase">Saved Measurements</span>
            <div className="flex bg-[#160408] p-0.5 rounded-lg border border-[#D4AF37]/30 text-[10px]">
              <button
                type="button"
                onClick={() => setUnit('in')}
                className={`px-2 py-0.5 rounded ${unit === 'in' ? 'bg-[#D4AF37] text-[#160408] font-bold' : 'text-[#C4B6A6]'}`}
              >
                in
              </button>
              <button
                type="button"
                onClick={() => setUnit('cm')}
                className={`px-2 py-0.5 rounded ${unit === 'cm' ? 'bg-[#D4AF37] text-[#160408] font-bold' : 'text-[#C4B6A6]'}`}
              >
                cm
              </button>
            </div>
          </div>

          <div className="grid grid-cols-4 gap-1.5">
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Chest</label>
              <input value={chest} onChange={(e) => setChest(e.target.value)} placeholder="36" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Waist</label>
              <input value={waist} onChange={(e) => setWaist(e.target.value)} placeholder="30" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Hips</label>
              <input value={hips} onChange={(e) => setHips(e.target.value)} placeholder="38" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Shoulder</label>
              <input value={shoulder} onChange={(e) => setShoulder(e.target.value)} placeholder="14.5" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Sleeve</label>
              <input value={sleeve} onChange={(e) => setSleeve(e.target.value)} placeholder="18" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Trouser</label>
              <input value={trouserLength} onChange={(e) => setTrouserLength(e.target.value)} placeholder="42" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Neck</label>
              <input value={neck} onChange={(e) => setNeck(e.target.value)} placeholder="7" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
            <div>
              <label className="text-[9px] text-[#C4B6A6] block mb-0.5">Armhole</label>
              <input value={armhole} onChange={(e) => setArmhole(e.target.value)} placeholder="16" className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-center text-[#F8F1E5]" />
            </div>
          </div>

          <div>
            <label className="text-[10px] text-[#C4B6A6] block mb-0.5">Fitting Notes</label>
            <input 
              value={notes} 
              onChange={(e) => setNotes(e.target.value)} 
              placeholder="e.g. Flare bottoms, loose sleeves"
              className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-lg p-1.5 text-[#F8F1E5]"
            />
          </div>
        </div>

        <button 
          type="submit" 
          className="w-full bg-[#D4AF37] hover:bg-[#F5D77F] text-[#160408] font-bold py-2.5 rounded-lg text-xs transition shadow-lg mt-2"
        >
          Save Client Profile
        </button>
      </form>
    </Modal>
  );
}
