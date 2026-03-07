import React from 'react';

const ChatScreen: React.FC = () => {
  return (
    <div className="flex h-screen overflow-hidden bg-background-light dark:bg-background-dark text-slate-900 dark:text-slate-100 font-display">
      {/* Sidebar Nav (same as feed for consistency) */}
       <aside className="w-20 lg:w-72 border-r border-slate-200 dark:border-slate-800 flex flex-col p-4 lg:p-6 gap-8 bg-background-light dark:bg-background-dark z-20">
        <div className="flex items-center gap-3 px-2">
           <div className="bg-primary p-2 rounded-xl flex items-center justify-center text-white shrink-0">
            <span className="material-symbols-outlined">auto_awesome</span>
          </div>
          <h1 className="text-xl font-extrabold tracking-tight hidden lg:block">Synapse</h1>
        </div>
        <nav className="flex flex-col gap-2">
           {/* Simple icons for smaller layout, text for large */}
           <a className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all font-semibold" href="#">
            <span className="material-symbols-outlined">home</span>
            <span className="hidden lg:block">Home</span>
          </a>
          <a className="flex items-center gap-4 px-4 py-3 rounded-xl bg-primary/10 text-primary font-semibold transition-all" href="#">
            <span className="material-symbols-outlined">chat_bubble</span>
            <span className="hidden lg:block">Messages</span>
          </a>
        </nav>
      </aside>

      {/* Chat Interface (Three-Pane) */}
      <div className="flex-1 flex overflow-hidden">
        {/* Pane 1: Conversation List */}
        <aside className="w-80 border-r border-slate-200 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-900/20 backdrop-blur-sm flex flex-col">
          <header className="p-6 border-b border-slate-200 dark:border-slate-800">
             <h2 className="text-xl font-bold">Messages</h2>
             <div className="mt-4 relative">
                <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-sm">search</span>
                <input className="w-full bg-slate-200/50 dark:bg-slate-800/50 border-none rounded-lg pl-10 pr-4 py-2 text-xs focus:ring-1 focus:ring-primary/50 outline-none" placeholder="Search conversations..." type="text"/>
             </div>
          </header>
          <div className="flex-1 overflow-y-auto">
             {/* Example Chats */}
             {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className={`p-4 flex gap-3 cursor-pointer hover:bg-slate-100 dark:hover:bg-slate-800/40 transition-colors ${i === 1 ? 'bg-primary/5 border-l-4 border-primary' : ''}`}>
                   <div className="h-12 w-12 rounded-full bg-slate-400 shrink-0 relative">
                      <div className="absolute bottom-0 right-0 w-3 h-3 bg-green-500 border-2 border-white dark:border-slate-900 rounded-full"></div>
                   </div>
                   <div className="flex-1 min-w-0">
                      <div className="flex justify-between items-center mb-1">
                         <h4 className="font-bold text-sm truncate">User {i}</h4>
                         <span className="text-[10px] text-slate-500">12:30 PM</span>
                      </div>
                      <p className="text-xs text-slate-500 truncate">Hey, check out this design approach...</p>
                   </div>
                </div>
             ))}
          </div>
        </aside>

        {/* Pane 2: Active Chat Area */}
        <main className="flex-1 flex flex-col relative bg-slate-50 dark:bg-[#0c0c1a]">
           <div className="absolute top-0 left-0 w-full h-full bg-primary/5 blur-[120px] pointer-events-none opacity-20"></div>
           
           <header className="px-8 py-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center backdrop-blur-md bg-white/30 dark:bg-slate-950/30 z-10">
              <div className="flex items-center gap-3">
                 <div className="h-10 w-10 rounded-full bg-slate-400"></div>
                 <div>
                    <h4 className="font-bold text-sm">Marcus Thorne</h4>
                    <p className="text-[10px] text-green-500 font-bold flex items-center gap-1">
                       <span className="w-1.5 h-1.5 bg-green-500 rounded-full"></span> Online
                    </p>
                 </div>
              </div>
              <div className="flex gap-4">
                 <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">call</span></button>
                 <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">videocam</span></button>
                 <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">info</span></button>
              </div>
           </header>

           <div className="flex-1 p-8 overflow-y-auto flex flex-col gap-6 relative z-0">
              {/* Message Bubbles */}
              <div className="flex flex-col gap-2 max-w-[70%]">
                 <div className="bg-white/40 dark:bg-slate-800/60 backdrop-blur-xl p-4 rounded-2xl rounded-tl-none border border-white/20 dark:border-slate-700/50 shadow-sm">
                    <p className="text-sm">Hey, have you seen the new design system update in the shared repo?</p>
                 </div>
                 <span className="text-[10px] text-slate-500 ml-1">10:45 AM</span>
              </div>

              <div className="flex flex-col gap-2 max-w-[70%] self-end items-end">
                 <div className="bg-primary/80 text-white p-4 rounded-2xl rounded-tr-none shadow-lg shadow-primary/20">
                    <p className="text-sm">Just checking it now! The glassmorphism looks incredibly clean on the web prototype.</p>
                 </div>
                 <span className="text-[10px] text-slate-500 mr-1">10:48 AM</span>
              </div>
           </div>

           <footer className="p-6 bg-white/30 dark:bg-slate-950/30 backdrop-blur-md border-t border-slate-200 dark:border-slate-800 z-10">
              <div className="max-w-4xl mx-auto flex gap-4 items-center">
                 <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">add</span></button>
                 <div className="flex-1 bg-white/50 dark:bg-slate-900/50 rounded-xl flex items-center px-4 py-2 border border-slate-200 dark:border-slate-800">
                    <input className="w-full bg-transparent border-none focus:ring-0 text-sm outline-none" placeholder="Type your message..." type="text"/>
                    <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">sentiment_satisfied</span></button>
                 </div>
                 <button className="h-10 w-10 bg-primary text-white rounded-xl flex items-center justify-center shadow-lg shadow-primary/25 hover:bg-primary/90 transition-all">
                    <span className="material-symbols-outlined">send</span>
                 </button>
              </div>
           </footer>
        </main>
      </div>
    </div>
  );
};

export default ChatScreen;
