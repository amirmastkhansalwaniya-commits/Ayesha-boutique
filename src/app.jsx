import React, { useState, useEffect } from 'react';
import { 
  Scissors, Plus, Search, Settings, Phone, Calendar, 
  DollarSign, CheckCircle2, AlertCircle, Clock, Users, 
  ShoppingBag, Trash2, Edit3, Image as ImageIcon, Sparkles, 
  ChevronRight, Printer, Share2, ShieldCheck, X
} from 'lucide-react';

export default function AyshaBoutiqueApp() {
  // Navigation State
  const [currentTab, setCurrentTab] = useState('dashboard'); // dashboard | orders | clients | finances | settings
  const [searchQuery, setSearchQuery] = useState('');

  // Settings State
  const [settings, setSettings] = useState(() => {
    const saved = localStorage.getItem('aysha_settings');
    return saved ? JSON.parse(saved) : {
      boutiqueName: 'AYSHA BOUTIQUE',
      tagline: 'Haute Couture & Bespoke Tailoring',
      currency: '₹',
      selectedLogo: '✂️',
      customLogoUrl: null,
      creatorCredit: 'This app created by Amir Khan'
    };
  });

  // Orders State
  const [orders, setOrders] = useState(() => {
    const saved = localStorage.getItem('aysha_orders');
    return saved ? JSON.parse(saved) : [
      {
        id: 1,
        orderNumber: 'AY-1001',
        customerName: 'Fatima Sheikh',
        customerPhone: '+91 98765 43210',
        suitType: 'Bridal Lehanga & Embellished Choli',
        fabricDetails: 'Pure Raw Silk with Zardozi Hand Embroidery',
        numberOfSuits: 1,
        standardSize: 'M (38)',
        totalAmount: 18500,
        receivedAmount: 12000,
        dateTaken: '2026-08-10',
        dateDueForDelivery: '2026-08-20',
        status: 'Stitching', // Order Taken | Cutting | Stitching | Fitting | Ready | Delivered
        measurements: { chest: '36', waist: '30', hips: '38', shoulder: '14.5', length: '42' }
      },
      {
        id: 2,
        orderNumber: 'AY-1002',
        customerName: 'Zainab Qureshi',
        customerPhone: '+91 91234 56789',
        suitType: 'Embroidered Anarkali Suit',
        fabricDetails: 'Georgette with Resham Threadwork',
        numberOfSuits: 2,
        standardSize: 'L (40)',
        totalAmount: 9500,
        receivedAmount: 9500,
        dateTaken: '2026-08-12',
        dateDueForDelivery: '2026-08-24',
        status: 'Fitting',
        measurements: { chest: '38', waist: '32', hips: '40', shoulder: '15', length: '44' }
      }
    ];
  });

  // Clients State
  const [clients, setClients] = useState(() => {
    const saved = localStorage.getItem('aysha_clients');
    return saved ? JSON.parse(saved) : [
      { id: 1, name: 'Fatima Sheikh', phone: '+91 98765 43210', chest: '36', waist: '30', hips: '38', address: 'Civil Lines, Jaipur' },
      { id: 2, name: 'Zainab Qureshi', phone: '+91 91234 56789', chest: '38', waist: '32', hips: '40', address: 'Old City, Delhi' }
    ];
  });

  // Modal Dialogs
  const [showNewOrderModal, setShowNewOrderModal] = useState(false);
  const [showNewClientModal, setShowNewClientModal] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);

  // Persistence
  useEffect(() => {
    localStorage.setItem('aysha_settings', JSON.stringify(settings));
  }, [settings]);

  useEffect(() => {
    localStorage.setItem('aysha_orders', JSON.stringify(orders));
  }, [orders]);

  useEffect(() => {
    localStorage.setItem('aysha_clients', JSON.stringify(clients));
  }, [clients]);

  // Financial Stats
  const totalRevenue = orders.reduce((acc, o) => acc + Number(o.totalAmount || 0), 0);
  const totalCollected = orders.reduce((acc, o) => acc + Number(o.receivedAmount || 0), 0);
  const totalPending = totalRevenue - totalCollected;

  // Filtered Orders
  const filteredOrders = orders.filter(o => 
    o.customerName.toLowerCase().includes(searchQuery.toLowerCase()) ||
    o.orderNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
    o.suitType.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const STATUS_STEPS = ['Order Taken', 'Cutting', 'Stitching', 'Fitting', 'Ready', 'Delivered'];

  // Handle Logo Upload
  const handleLogoUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        setSettings(prev => ({ ...prev, customLogoUrl: event.target.result }));
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <div className="min-h-screen bg-[#160408] text-[#F8F1E5] flex justify-center font-sans antialiased">
      <div className="w-full max-w-lg min-h-screen bg-[#160408] border-x border-[#D4AF37]/20 flex flex-col relative shadow-2xl pb-24">
        
        {/* HEADER / TOPBAR */}
        <header className="sticky top-0 z-40 bg-gradient-to-b from-[#160408] to-[#2E0B13] border-b border-[#D4AF37]/30 p-4 backdrop-blur-md">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              {/* Logo / Emblem */}
              <div className="w-12 h-12 rounded-full border-2 border-[#D4AF37] bg-[#3A0F1A] flex items-center justify-center overflow-hidden shadow-inner flex-shrink-0">
                {settings.customLogoUrl ? (
                  <img src={settings.customLogoUrl} alt="Logo" className="w-full h-full object-cover" />
                ) : (
                  <span className="text-2xl">{settings.selectedLogo}</span>
                )}
              </div>
              <div>
                <h1 className="text-base font-bold tracking-widest text-[#F5D77F] font-serif uppercase">
                  {settings.boutiqueName}
                </h1>
                <p className="text-xs text-[#C4B6A6] tracking-wide truncate max-w-[200px]">
                  {settings.tagline}
                </p>
              </div>
            </div>

            <button 
              onClick={() => setCurrentTab('settings')}
              className="w-10 h-10 rounded-xl bg-[#2E0B13] border border-[#D4AF37]/50 flex items-center justify-center text-[#F5D77F] hover:bg-[#3A0F1A] transition"
            >
              <Settings className="w-5 h-5" />
            </button>
          </div>

          {/* Search Bar */}
          {currentTab !== 'settings' && (
            <div className="mt-3 relative">
              <Search className="w-4 h-4 absolute left-3.5 top-3 text-[#D4AF37]/70" />
              <input 
                type="text"
                placeholder="Search orders, clients, suits..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full bg-[#2E0B13] border border-[#D4AF37]/30 rounded-xl pl-10 pr-4 py-2 text-sm text-[#F8F1E5] focus:outline-none focus:border-[#D4AF37] placeholder-[#C4B6A6]/50"
              />
            </div>
          )}
        </header>

        {/* MAIN BODY VIEW */}
        <main className="p-4 flex-1 space-y-4 overflow-y-auto">
          
          {/* TAB 1: DASHBOARD (ATELIER) */}
          {currentTab === 'dashboard' && (
            <>
              {/* Hero Banner */}
              <div className="rounded-2xl p-5 bg-gradient-to-b from-[#521524]/60 to-[#3A0F1A] border border-[#D4AF37]/40 shadow-lg">
                <span className="text-[10px] font-bold tracking-wider text-[#F5D77F] uppercase block">
                  {new Date().toLocaleDateString('hi-IN', { weekday: 'long', month: 'short', day: 'numeric' })}
                </span>
                <h2 className="text-xl font-serif font-bold text-[#F8F1E5] mt-1">{settings.boutiqueName} Atelier</h2>
                <p className="text-xs text-[#C4B6A6] mt-0.5">Bespoke Tailoring, Order Book & Measurement Ledger</p>

                <div className="grid grid-cols-2 gap-2.5 mt-4">
                  <button 
                    onClick={() => setShowNewOrderModal(true)}
                    className="flex items-center justify-center gap-2 bg-[#D4AF37] hover:bg-[#F5D77F] text-[#160408] font-bold text-xs py-2.5 px-3 rounded-xl transition shadow"
                  >
                    <Plus className="w-4 h-4" /> New Order
                  </button>
                  <button 
                    onClick={() => setShowNewClientModal(true)}
                    className="flex items-center justify-center gap-2 bg-[#2E0B13] hover:bg-[#3A0F1A] border border-[#D4AF37]/50 text-[#F5D77F] font-bold text-xs py-2.5 px-3 rounded-xl transition"
                  >
                    <Users className="w-4 h-4" /> New Client
                  </button>
                </div>
              </div>

              {/* Atelier Statistics */}
              <div className="grid grid-cols-2 gap-3">
                <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3 flex justify-between items-center">
                  <div>
                    <span className="text-xs text-[#C4B6A6]">Active Orders</span>
                    <div className="text-xl font-bold text-[#F8F1E5] mt-0.5">{orders.length}</div>
                  </div>
                  <div className="w-9 h-9 rounded-full bg-[#521524] flex items-center justify-center text-[#F5D77F]">
                    <ShoppingBag className="w-5 h-5" />
                  </div>
                </div>
                <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3 flex justify-between items-center">
                  <div>
                    <span className="text-xs text-[#C4B6A6]">Total Billed</span>
                    <div className="text-xl font-bold text-[#F8F1E5] mt-0.5">{settings.currency}{totalRevenue.toLocaleString()}</div>
                  </div>
                  <div className="w-9 h-9 rounded-full bg-[#521524] flex items-center justify-center text-[#10B981]">
                    <DollarSign className="w-5 h-5" />
                  </div>
                </div>
              </div>

              {/* Financial Quick Card */}
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-2xl p-4">
                <div className="flex justify-between items-center mb-3">
                  <span className="text-xs font-bold tracking-wider text-[#F5D77F] uppercase">Financial Accounting</span>
                  <span className="text-[11px] bg-[#160408] px-2.5 py-1 rounded-full text-[#10B981] font-semibold">
                    {totalRevenue ? Math.round((totalCollected / totalRevenue) * 100) : 0}% Collected
                  </span>
                </div>
                <div className="grid grid-cols-3 gap-2 text-center">
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#D4AF37]/20">
                    <span className="text-[10px] text-[#C4B6A6] block">Billed</span>
                    <span className="text-sm font-bold text-[#F8F1E5]">{settings.currency}{totalRevenue}</span>
                  </div>
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#10B981]/30">
                    <span className="text-[10px] text-[#C4B6A6] block">Received</span>
                    <span className="text-sm font-bold text-[#10B981]">{settings.currency}{totalCollected}</span>
                  </div>
                  <div className="bg-[#160408] p-2.5 rounded-xl border border-[#F59E0B]/30">
                    <span className="text-[10px] text-[#C4B6A6] block">Pending</span>
                    <span className="text-sm font-bold text-[#F59E0B]">{settings.currency}{totalPending}</span>
                  </div>
                </div>
              </div>

              {/* Active Orders List */}
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <span className="text-xs font-bold tracking-wider text-[#F5D77F] uppercase">Recent Atelier Orders</span>
                  <button onClick={() => setCurrentTab('orders')} className="text-xs text-[#D4AF37] hover:underline flex items-center gap-1">
                    View All <ChevronRight className="w-3.5 h-3.5" />
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
              </div>
            </>
          )}

          {/* TAB 2: ALL ORDERS */}
          {currentTab === 'orders' && (
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <h2 className="text-sm font-bold text-[#F5D77F] uppercase tracking-wider">All Tailoring Orders ({filteredOrders.length})</h2>
                <button 
                  onClick={() => setShowNewOrderModal(true)}
                  className="bg-[#D4AF37] text-[#160408] text-xs font-bold px-3 py-1.5 rounded-lg flex items-center gap-1"
                >
                  <Plus className="w-3.5 h-3.5" /> Add Order
                </button>
              </div>

              {filteredOrders.map(order => (
                <OrderCard 
                  key={order.id} 
                  order={order} 
                  currency={settings.currency} 
                  onView={() => setSelectedOrder(order)} 
                />
              ))}
            </div>
          )}

          {/* TAB 3: CLIENT DIRECTORY */}
          {currentTab === 'clients' && (
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <h2 className="text-sm font-bold text-[#F5D77F] uppercase tracking-wider">Client Directory ({clients.length})</h2>
                <button 
                  onClick={() => setShowNewClientModal(true)}
                  className="bg-[#D4AF37] text-[#160408] text-xs font-bold px-3 py-1.5 rounded-lg flex items-center gap-1"
                >
                  <Plus className="w-3.5 h-3.5" /> Add Client
                </button>
              </div>

              {clients.map(client => (
                <div key={client.id} className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-xl p-3.5 space-y-2">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-bold text-sm text-[#F8F1E5]">{client.name}</h3>
                      <p className="text-xs text-[#C4B6A6]">{client.address || 'Address Not Provided'}</p>
                    </div>
                    <a href={`tel:${client.phone}`} className="p-2 rounded-lg bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F5D77F]">
                      <Phone className="w-4 h-4" />
                    </a>
                  </div>
                  <div className="bg-[#160408] p-2 rounded-lg border border-[#D4AF37]/20 flex justify-between text-[11px] text-[#F5D77F]">
                    <span>Chest: <b>{client.chest || '-'}″</b></span>
                    <span>Waist: <b>{client.waist || '-'}″</b></span>
                    <span>Hips: <b>{client.hips || '-'}″</b></span>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* TAB 4: FINANCES */}
          {currentTab === 'finances' && (
            <div className="space-y-4">
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-2xl p-5 space-y-4">
                <h2 className="text-sm font-bold text-[#F5D77F] uppercase tracking-wider">Financial Overview</h2>
                <div className="grid grid-cols-3 gap-2 text-center">
                  <div className="bg-[#160408] p-3 rounded-xl border border-[#D4AF37]/20">
                    <span className="text-[11px] text-[#C4B6A6] block">Total Billed</span>
                    <span className="text-base font-bold text-[#F8F1E5]">{settings.currency}{totalRevenue}</span>
                  </div>
                  <div className="bg-[#160408] p-3 rounded-xl border border-[#10B981]/30">
                    <span className="text-[11px] text-[#C4B6A6] block">Received</span>
                    <span className="text-base font-bold text-[#10B981]">{settings.currency}{totalCollected}</span>
                  </div>
                  <div className="bg-[#160408] p-3 rounded-xl border border-[#F59E0B]/30">
                    <span className="text-[11px] text-[#C4B6A6] block">Pending</span>
                    <span className="text-base font-bold text-[#F59E0B]">{settings.currency}{totalPending}</span>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* TAB 5: SETTINGS */}
          {currentTab === 'settings' && (
            <div className="space-y-4">
              <div className="bg-[#3A0F1A] border border-[#D4AF37]/30 rounded-2xl p-4 space-y-3.5">
                <h2 className="text-sm font-bold text-[#F5D77F] uppercase tracking-wider">App & Boutique Settings</h2>
                
                <div>
                  <label className="text-xs text-[#C4B6A6] block mb-1">Boutique Name</label>
                  <input 
                    type="text" 
                    value={settings.boutiqueName} 
                    onChange={(e) => setSettings(prev => ({ ...prev, boutiqueName: e.target.value }))}
                    className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-xl px-3 py-2 text-sm text-[#F8F1E5] focus:outline-none"
                  />
                </div>

                <div>
                  <label className="text-xs text-[#C4B6A6] block mb-1">Tagline</label>
                  <input 
                    type="text" 
                    value={settings.tagline} 
                    onChange={(e) => setSettings(prev => ({ ...prev, tagline: e.target.value }))}
                    className="w-full bg-[#160408] border border-[#D4AF37]/40 rounded-xl px-3 py-2 text-sm text-[#F8F1E5] focus:outline-none"
                  />
                </div>

                <div>
                  <label className="text-xs text-[#C4B6A6] block mb-1">Currency Symbol</label>
                  <div className="flex gap-2">
                    {['₹', '$', 'AED', 'SAR', '£'].map(c => (
                      <button 
                        key={c}
                        onClick={() => setSettings(prev => ({ ...prev, currency: c }))}
                        className={`flex-1 py-1.5 rounded-lg text-xs font-bold border transition ${
                          settings.currency === c ? 'bg-[#D4AF37] text-[#160408] border-[#D4AF37]' : 'bg-[#160408] text-[#F8F1E5] border-[#D4AF37]/30'
                        }`}
                      >
                        {c}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Logo & Photo Picker */}
                <div>
                  <label className="text-xs text-[#C4B6A6] block mb-1">App Emblem / Photo from Gallery</label>
                  <div className="flex items-center gap-3">
                    <div className="w-14 h-14 rounded-full border-2 border-[#D4AF37] bg-[#160408] flex items-center justify-center overflow-hidden flex-shrink-0 text-2xl">
                      {settings.customLogoUrl ? (
                        <img src={settings.customLogoUrl} alt="Logo" className="w-full h-full object-cover" />
                      ) : (
                        settings.selectedLogo
                      )}
                    </div>
                    <div className="flex-1 space-y-1.5">
                      <label className="flex items-center justify-center gap-2 bg-[#D4AF37] text-[#160408] font-bold text-xs py-2 px-3 rounded-xl cursor-pointer hover:bg-[#F5D77F] transition">
                        <ImageIcon className="w-4 h-4" /> Pick from Gallery
                        <input type="file" accept="image/*" className="hidden" onChange={handleLogoUpload} />
                      </label>
                      {settings.customLogoUrl && (
                        <button 
                          onClick={() => setSettings(prev => ({ ...prev, customLogoUrl: null }))}
                          className="w-full bg-[#160408] border border-[#D4AF37]/30 text-xs py-1 rounded-lg text-[#C4B6A6]"
                        >
                          Remove Photo (Use Icon)
                        </button>
                      )}
                    </div>
                  </div>

                  {/* Preset Icons */}
                  <div className="flex gap-2 mt-3">
                    {['✂️', '🪡', '👑', '👗', '💎', '🧵'].map(ico => (
                      <button 
                        key={ico}
                        onClick={() => setSettings(prev => ({ ...prev, selectedLogo: ico, customLogoUrl: null }))}
                        className={`flex-1 py-2 rounded-xl text-lg border transition ${
                          settings.selectedLogo === ico && !settings.customLogoUrl ? 'bg-[#521524] border-[#D4AF37]' : 'bg-[#160408] border-[#D4AF37]/20'
                        }`}
                      >
                        {ico}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              {/* Creator Card */}
              <div className="rounded-2xl p-5 bg-gradient-to-b from-[#2E0B13] to-[#160408] border-2 border-[#D4AF37] text-center space-y-1">
                <div className="flex items-center justify-center gap-1.5 text-[#F5D77F] text-xs font-bold tracking-widest uppercase">
                  <Sparkles className="w-3.5 h-3.5" /> Official Boutique Software <Sparkles className="w-3.5 h-3.5" />
                </div>
                <h3 className="text-lg font-serif font-bold text-[#F5D77F] tracking-wide mt-1">
                  This app created by Amir Khan
                </h3>
                <p className="text-xs text-[#C4B6A6]">Crafted with bespoke precision for Aysha Boutique Atelier</p>
                <div className="inline-flex items-center gap-1.5 bg-[#3A0F1A] border border-[#10B981]/50 px-3 py-1 rounded-full text-[11px] text-[#10B981] font-semibold mt-2">
                  <ShieldCheck className="w-3.5 h-3.5" /> Version 1.0.0 • Local Secure Storage
                </div>
              </div>
            </div>
          )}

          {/* MANDATORY CREATOR FOOTER ON DASHBOARD */}
          {currentTab === 'dashboard' && (
            <div className="rounded-xl p-4 bg-[#2E0B13] border border-[#D4AF37]/50 text-center space-y-0.5">
              <h4 className="text-sm font-serif font-bold text-[#F5D77F]">This app created by Amir Khan</h4>
              <p className="text-[11px] text-[#C4B6A6]">Official Bespoke Tailoring Suite</p>
            </div>
          )}
        </main>

        {/* BOTTOM NAVIGATION BAR */}
        <nav className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-lg bg-[#22070D] border-t border-[#D4AF37]/40 py-2 px-3 flex justify-around items-center z-50 shadow-2xl">
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
                className={`flex flex-col items-center gap-1 py-1 px-3 rounded-xl transition ${
                  isActive ? 'bg-[#D4AF37] text-[#160408] font-bold shadow' : 'text-[#C4B6A6] hover:text-[#F8F1E5]'
                }`}
              >
                <Icon className="w-4 h-4" />
                <span className="text-[10px]">{tab.label}</span>
              </button>
            );
          })}
        </nav>

        {/* MODAL: NEW ORDER FORM */}
        {showNewOrderModal && (
          <Modal title="Create New Tailoring Order" onClose={() => setShowNewOrderModal(false)}>
            <form onSubmit={(e) => {
              e.preventDefault();
              const fd = new FormData(e.target);
              const total = Number(fd.get('totalAmount') || 0);
              const received = Number(fd.get('receivedAmount') || 0);
              const newOrder = {
                id: Date.now(),
                orderNumber: 'AY-' + Math.floor(1000 + Math.random() * 9000),
                customerName: fd.get('customerName'),
                customerPhone: fd.get('customerPhone'),
                suitType: fd.get('suitType'),
                fabricDetails: fd.get('fabricDetails'),
                numberOfSuits: Number(fd.get('numberOfSuits') || 1),
                standardSize: fd.get('standardSize'),
                totalAmount: total,
                receivedAmount: received,
                dateTaken: new Date().toISOString().split('T')[0],
                dateDueForDelivery: fd.get('dueDate'),
                status: 'Order Taken',
                measurements: {
                  chest: fd.get('chest') || '-',
                  waist: fd.get('waist') || '-',
                  hips: fd.get('hips') || '-',
                  shoulder: fd.get('shoulder') || '-',
                  length: fd.get('length') || '-'
                }
              };
              setOrders([newOrder, ...orders]);
              setShowNewOrderModal(false);
            }} className="space-y-3">
              <div>
                <label className="text-xs text-[#F5D77F] font-bold block mb-1">Customer Full Name *</label>
                <input required name="customerName" placeholder="e.g. Fatima Sheikh" className="modal-input" />
              </div>
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="text-xs text-[#F5D77F] font-bold block mb-1">Phone Number *</label>
                  <input required name="customerPhone" placeholder="+91 " className="modal-input" />
                </div>
                <div>
                  <label className="text-xs text-[#F5D77F] font-bold block mb-1">Delivery Due Date *</label>
                  <input required type="date" name="dueDate" className="modal-input" />
                </div>
              </div>
              <div>
                <label className="text-xs text-[#F5D77F] font-bold block mb-1">Suit / Garment Type *</label>
                <input required name="suitType" placeholder="e.g. Bridal Lehanga / Anarkali" className="modal-input" />
              </div>
              <div>
                <label className="text-xs text-[#F5D77F] font-bold block mb-1">Fabric & Design Details</label>
                <input name="fabricDetails" placeholder="e.g. Pure Silk, Zardozi Work" className="modal-input" />
              </div>

              {/* Measurements row */}
              <div className="bg-[#160408] p-3 rounded-xl border border-[#D4AF37]/30 space-y-2">
                <span className="text-[11px] font-bold text-[#F5D77F] block">Body Measurements (Inches)</span>
                <div className="grid grid-cols-5 gap-1.5 text-center">
                  <input name="chest" placeholder="Chest" className="modal-input-sm" />
                  <input name="waist" placeholder="Waist" className="modal-input-sm" />
                  <input name="hips" placeholder="Hips" className="modal-input-sm" />
                  <input name="shoulder" placeholder="Shldr" className="modal-input-sm" />
                  <input name="length" placeholder="Length" className="modal-input-sm" />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="text-xs text-[#F5D77F] font-bold block mb-1">Total Bill ({settings.currency}) *</label>
                  <input required type="number" name="totalAmount" placeholder="3500" className="modal-input" />
                </div>
                <div>
                  <label className="text-xs text-[#F5D77F] font-bold block mb-1">Advance Received ({settings.currency})</label>
                  <input type="number" name="receivedAmount" placeholder="1500" className="modal-input" />
                </div>
              </div>

              <button type="submit" className="w-full bg-[#D4AF37] hover:bg-[#F5D77F] text-[#160408] font-bold py-2.5 rounded-xl text-sm transition mt-2">
                Book Order in Atelier
              </button>
            </form>
          </Modal>
        )}

        {/* MODAL: ORDER DETAILS & INVOICE */}
        {selectedOrder && (
          <Modal title={`Invoice: ${selectedOrder.orderNumber}`} onClose={() => setSelectedOrder(null)}>
            <div className="space-y-4">
              <div className="flex justify-between items-start border-b border-[#D4AF37]/30 pb-3">
                <div>
                  <h3 className="font-serif font-bold text-lg text-[#F5D77F]">{selectedOrder.suitType}</h3>
                  <p className="text-xs text-[#C4B6A6]">Client: {selectedOrder.customerName} ({selectedOrder.customerPhone})</p>
                  <p className="text-xs text-[#C4B6A6]">Delivery Due: <b>{selectedOrder.dateDueForDelivery}</b></p>
                </div>
                <span className="bg-[#521524] text-[#F5D77F] border border-[#D4AF37]/40 px-2.5 py-1 rounded-full text-xs font-bold">
                  {selectedOrder.status}
                </span>
              </div>

              {/* Status Stepper */}
              <div className="space-y-1.5">
                <span className="text-[11px] font-bold text-[#F5D77F] block">Tailoring Production Stage</span>
                <div className="grid grid-cols-6 gap-1 text-center">
                  {STATUS_STEPS.map(st => {
                    const isCurrent = selectedOrder.status === st;
                    return (
                      <button
                        key={st}
                        onClick={() => {
                          const updated = orders.map(o => o.id === selectedOrder.id ? { ...o, status: st } : o);
                          setOrders(updated);
                          setSelectedOrder({ ...selectedOrder, status: st });
                        }}
                        className={`text-[9px] py-1.5 rounded border ${
                          isCurrent ? 'bg-[#D4AF37] text-[#160408] font-bold border-[#D4AF37]' : 'bg-[#160408] text-[#C4B6A6] border-[#D4AF37]/20'
                        }`}
                      >
                        {st}
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Measurements */}
              <div className="bg-[#160408] p-3 rounded-xl border border-[#D4AF37]/20 flex justify-between text-xs text-[#F5D77F]">
                <span>Chest: <b>{selectedOrder.measurements?.chest}″</b></span>
                <span>Waist: <b>{selectedOrder.measurements?.waist}″</b></span>
                <span>Hips: <b>{selectedOrder.measurements?.hips}″</b></span>
                <span>Shoulder: <b>{selectedOrder.measurements?.shoulder}″</b></span>
              </div>

              {/* Financial Breakdown */}
              <div className="bg-[#160408] p-3 rounded-xl border border-[#D4AF37]/30 space-y-1.5 text-xs">
                <div className="flex justify-between">
                  <span className="text-[#C4B6A6]">Total Billed:</span>
                  <span className="font-bold text-[#F8F1E5]">{settings.currency}{selectedOrder.totalAmount}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-[#C4B6A6]">Advance Paid:</span>
                  <span className="font-bold text-[#10B981]">{settings.currency}{selectedOrder.receivedAmount}</span>
                </div>
                <div className="flex justify-between border-t border-[#D4AF37]/20 pt-1.5">
                  <span className="text-[#C4B6A6]">Pending Due:</span>
                  <span className="font-bold text-[#F59E0B]">{settings.currency}{selectedOrder.totalAmount - selectedOrder.receivedAmount}</span>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="flex gap-2">
                <a 
                  href={`tel:${selectedOrder.customerPhone}`}
                  className="flex-1 bg-[#2E0B13] border border-[#D4AF37]/40 text-[#F5D77F] py-2 rounded-xl text-xs font-bold flex items-center justify-center gap-1.5"
                >
                  <Phone className="w-3.5 h-3.5" /> Call Client
                </a>
                <button 
                  onClick={() => {
                    const text = `*${settings.boutiqueName} - Invoice*\nOrder No: ${selectedOrder.orderNumber}\nClient: ${selectedOrder.customerName}\nSuit: ${selectedOrder.suitType}\nTotal: ${settings.currency}${selectedOrder.totalAmount}\nPaid: ${settings.currency}${selectedOrder.receivedAmount}\nPending: ${settings.currency}${selectedOrder.totalAmount - selectedOrder.receivedAmount}\nDue Date: ${selectedOrder.dateDueForDelivery}\n\n_${settings.creatorCredit}_`;
                    navigator.clipboard?.writeText(text);
                    alert("Digital Receipt copied to clipboard!");
                  }}
                  className="flex-1 bg-[#D4AF37] text-[#160408] py-2 rounded-xl text-xs font-bold flex items-center justify-center gap-1.5"
                >
                  <Share2 className="w-3.5 h-3.5" /> Share Receipt
                </button>
              </div>
            </div>
          </Modal>
        )}

      </div>
    </div>
  );
}

// Sub-Component: Order Card
function OrderCard({ order, currency, onView }) {
  const pending = Number(order.totalAmount || 0) - Number(order.receivedAmount || 0);
  return (
    <div 
      onClick={onView}
      className="bg-[#3A0F1A] border border-[#D4AF37]/30 hover:border-[#D4AF37] rounded-xl p-3.5 space-y-2 cursor-pointer transition shadow-md"
    >
      <div className="flex justify-between items-center">
        <span className="bg-[#160408] border border-[#D4AF37]/50 text-[#F5D77F] text-[11px] font-mono px-2 py-0.5 rounded font-bold">
          {order.orderNumber}
        </span>
        <span className="text-[11px] bg-[#521524] text-[#F5D77F] px-2.5 py-0.5 rounded-full font-bold border border-[#D4AF37]/30">
          Due: {order.dateDueForDelivery}
        </span>
      </div>

      <div>
        <h3 className="font-serif font-bold text-sm text-[#F8F1E5]">{order.suitType}</h3>
        <p className="text-xs text-[#C4B6A6]">👤 {order.customerName} • {order.customerPhone}</p>
      </div>

      <div className="bg-[#160408] p-2 rounded-lg border border-[#D4AF37]/20 flex justify-between items-center text-xs">
        <span>Total: <b>{currency}{order.totalAmount}</b></span>
        <span>Paid: <b className="text-[#10B981]">{currency}{order.receivedAmount}</b></span>
        <span>Due: <b className={pending > 0 ? 'text-[#F59E0B]' : 'text-[#10B981]'}>{currency}{pending}</b></span>
      </div>
    </div>
  );
}

// Sub-Component: Modal Wrapper
function Modal({ title, onClose, children }) {
  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-[#2E0B13] border-2 border-[#D4AF37] rounded-2xl w-full max-w-md max-h-[90vh] overflow-y-auto p-5 relative shadow-2xl">
        <div className="flex justify-between items-center border-b border-[#D4AF37]/30 pb-3 mb-4">
          <h3 className="font-serif font-bold text-base text-[#F5D77F]">{title}</h3>
          <button onClick={onClose} className="text-[#C4B6A6] hover:text-[#F8F1E5]">
            <X className="w-5 h-5" />
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}
