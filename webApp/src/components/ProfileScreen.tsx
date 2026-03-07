import React from 'react';

const ProfileScreen: React.FC = () => {
  return (
    <div className="flex h-screen overflow-hidden bg-background-light dark:bg-background-dark text-slate-900 dark:text-slate-100 font-display">
      {/* Sidebar Nav (consistent) */}
      <aside className="w-72 border-r border-slate-200 dark:border-slate-800 flex flex-col p-6 gap-8 bg-background-light dark:bg-background-dark scrollbar-hide">
        <div className="flex items-center gap-3 px-2">
          <div className="bg-primary p-2 rounded-xl flex items-center justify-center text-white">
            <span className="material-symbols-outlined">auto_awesome</span>
          </div>
          <h1 className="text-xl font-extrabold tracking-tight">Synapse</h1>
        </div>
        <nav className="flex flex-col gap-2">
           <a className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all font-semibold" href="#">
            <span className="material-symbols-outlined">home</span>
            <span>Home</span>
          </a>
          <a className="flex items-center gap-4 px-4 py-3 rounded-xl bg-primary/10 text-primary font-semibold transition-all" href="#">
            <span className="material-symbols-outlined">person</span>
            <span>Profile</span>
          </a>
        </nav>
      </aside>

      {/* Profile Main View */}
      <main className="flex-1 overflow-y-auto bg-slate-50 dark:bg-[#0c0c1a] relative">
         <div className="absolute top-0 left-0 w-full h-80 bg-gradient-to-b from-primary/20 to-transparent blur-3xl opacity-30 pointer-events-none"></div>

         <div className="max-w-4xl mx-auto py-8 px-8 flex flex-col gap-8 relative z-10">
            {/* Header / Banner Area */}
            <div className="flex flex-col gap-6 relative">
               <div className="h-64 w-full rounded-2xl bg-slate-800 flex items-center justify-center text-slate-500 overflow-hidden relative border border-white/10">
                   {/* Banner Visual placeholder */}
                   <div className="absolute inset-0 bg-cover bg-center grayscale blur-md opacity-40 shadow-inner" style={{ backgroundImage: 'linear-gradient(45deg, var(--primary-violet), var(--primary-indigo))' }}></div>
                   <span className="relative z-10 font-bold tracking-widest uppercase text-xs opacity-50">Profile Neural Space</span>
               </div>
               
               <div className="flex justify-between items-end px-6 -mt-20">
                  <div className="flex gap-6 items-end">
                     <div className="h-32 w-32 rounded-3xl bg-slate-400 border-4 border-slate-900 shadow-2xl overflow-hidden shadow-primary/20">
                        {/* Profile Pic Placeholder */}
                        <div className="w-full h-full bg-cover bg-center" style={{ backgroundImage: 'linear-gradient(135deg, #eee, #ccc)' }}></div>
                     </div>
                     <div className="mb-2">
                        <h2 className="text-3xl font-extrabold tracking-tight">Ashik Ahmed</h2>
                        <p className="text-slate-400 font-medium">@ashik_ahmed</p>
                     </div>
                  </div>
                  <div className="flex gap-3 mb-2">
                     <button className="px-6 py-2 bg-primary/10 text-primary font-bold rounded-xl border border-primary/20 hover:bg-primary/20 transition-all">Edit Profile</button>
                     <button className="px-4 py-2 bg-slate-800/50 rounded-xl hover:bg-slate-800 transition-colors"><span className="material-symbols-outlined">settings</span></button>
                  </div>
               </div>
            </div>

            {/* Profile Content / Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
               <div className="md:col-span-2 flex flex-col gap-6">
                  {/* Tabs */}
                  <div className="flex gap-8 border-b border-slate-200 dark:border-slate-800 px-2">
                     <button className="pb-4 border-b-2 border-primary text-primary font-bold text-sm">Posts</button>
                     <button className="pb-4 border-b-2 border-transparent text-slate-500 hover:text-slate-300 transition-colors font-bold text-sm">Media</button>
                     <button className="pb-4 border-b-2 border-transparent text-slate-500 hover:text-slate-300 transition-colors font-bold text-sm">Highlights</button>
                  </div>
                  
                  {/* Grid Content */}
                  <div className="grid grid-cols-2 gap-4">
                     {[1, 2, 3, 4].map((i) => (
                        <div key={i} className="aspect-square bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-white/10 rounded-2xl flex items-center justify-center text-slate-500 text-xs font-bold hover:border-primary/50 transition-colors cursor-pointer group">
                           <span className="opacity-0 group-hover:opacity-100 transition-opacity">Design Node {i}</span>
                        </div>
                     ))}
                  </div>
               </div>

               {/* Right Sidebar - Stats / Bio */}
               <div className="flex flex-col gap-6">
                  <div className="bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-white/20 dark:border-slate-800/50 p-6 rounded-2xl flex flex-col gap-4 shadow-sm">
                     <h3 className="font-bold text-sm flex items-center gap-2"><span className="material-symbols-outlined text-primary text-xl">info</span> About</h3>
                     <p className="text-xs text-slate-400 leading-relaxed">Product Architect & Neural Explorer. Creating experiences that bridge human connection with advanced tech ecosystems. ✨</p>
                  </div>
                  
                  <div className="bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-white/20 dark:border-slate-800/50 p-6 rounded-2xl grid grid-cols-3 gap-4 shadow-sm text-center">
                     <div>
                        <h4 className="font-bold text-lg">1.2k</h4>
                        <p className="text-[10px] text-slate-500 uppercase tracking-widest font-bold">Posts</p>
                     </div>
                     <div>
                        <h4 className="font-bold text-lg">8.4k</h4>
                        <p className="text-[10px] text-slate-500 uppercase tracking-widest font-bold">Followers</p>
                     </div>
                     <div>
                        <h4 className="font-bold text-lg">452</h4>
                        <p className="text-[10px] text-slate-500 uppercase tracking-widest font-bold">Following</p>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </main>
    </div>
  );
};

export default ProfileScreen;
